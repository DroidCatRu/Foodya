package ru.droidcat.core_network_impl

import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.toJson
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import ru.droidcat.core_network_api.MutationResult
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.core_network_api.hydration.HydrationInfo
import ru.droidcat.core_network_api.meal.Meal
import ru.droidcat.core_network_api.meal.MealType
import ru.droidcat.core_network_api.recipe.RecipeBasic
import ru.droidcat.core_network_api.recipe.RecipeFull
import ru.droidcat.core_network_api.users.Restriction
import ru.droidcat.core_network_api.users.UserData
import ru.droidcat.core_network_api.users.UserGender
import ru.droidcat.core_network_impl.type.ActivityLevel
import ru.droidcat.core_network_impl.type.BiologicalSex
import ru.droidcat.core_network_impl.type.WeeklyWeightGoal
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.streams.toList

class NetworkRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDb: FirebaseDatabase
) : NetworkRepository {

    override suspend fun signUpUserEmailPassword(
        name: String,
        email: String,
        password: String
    ): MutationResult {
        if (firebaseAuth.currentUser != null) {
            return MutationResult.ERROR.AUTH.USER_ALREADY_SIGNED_IN
        }

        val firebaseAuthResult = try {
            withContext(Dispatchers.IO) {
                firebaseAuth
                    .createUserWithEmailAndPassword(email, password)
                    .await()
            }
        } catch (e: FirebaseAuthException) {
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> {
                    return if (e is FirebaseAuthWeakPasswordException) {
                        MutationResult.ERROR.AUTH.WEAK_PASSWORD
                    } else {
                        MutationResult.ERROR.AUTH.INVALID_CREDENTIALS
                    }
                }
                is FirebaseAuthUserCollisionException -> {
                    return MutationResult.ERROR.AUTH.USER_ALREADY_EXISTS
                }
                else -> {
                    return MutationResult.ERROR.AUTH.UNKNOWN
                }
            }
        }

        val user = firebaseAuthResult?.user ?: return MutationResult.ERROR.AUTH.UNKNOWN


        val apolloClient = NetworkService.getInstance()!!.getApolloClientWithAuthToken()

        val result = apolloClient
            .mutation(CreateUserMutation(name, email))
            .execute()

        if (result.data?.createUser?.user != null) {

            val databaseId = result.data!!.createUser!!.user!!.databaseId
            firebaseDb.reference.child("users").child(user.uid).setValue(databaseId)

            return MutationResult.SUCCESS(databaseId)
        } else {
            val errorMessage = result.errors!![0].message
            return when (errorMessage) {
                "400: Bad Request" -> MutationResult.ERROR.HTTP.BAD_REQUEST_400
                "403: Forbidden" -> MutationResult.ERROR.HTTP.FORBIDDEN_403
                "Not authorized to see this resource" -> MutationResult.ERROR.HTTP.NOT_AUTHORIZED
                else -> MutationResult.ERROR.HTTP.UNKNOWN
            }
        }
    }

    override suspend fun signInUserEmailPassword(
        email: String,
        password: String
    ): MutationResult {
        if (firebaseAuth.currentUser != null) {
            return MutationResult.ERROR.AUTH.USER_ALREADY_SIGNED_IN
        }

        val firebaseAuthResult =
            signInFirebaseWithEmail(
                email,
                password
            )

        val userUid = firebaseAuthResult?.user?.uid

        return if (userUid != null) {
            val databaseId = firebaseDb.reference.child("users").child(userUid).get().await()
                .getValue(String::class.java)

            if (databaseId.isNullOrBlank()) {
                MutationResult.ERROR.AUTH.UNKNOWN
            }

            MutationResult.SUCCESS(databaseId)
        } else {
            MutationResult.ERROR.AUTH.UNKNOWN
        }
    }

    override suspend fun removeUserProfile(databaseId: String): MutationResult {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .mutation(DeleteUserMutation())
            .execute()

        if (result.data != null) {

            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) return MutationResult.ERROR.AUTH.USER_NOT_SIGNED_IN

            firebaseDb.reference.child("users").child(currentUser.uid).removeValue().await()

            return MutationResult.SUCCESS()
        } else {
            val errorMessage = result.errors!![0].message
            return when (errorMessage) {
                "400: Bad Request" -> MutationResult.ERROR.HTTP.BAD_REQUEST_400
                "403: Forbidden" -> MutationResult.ERROR.HTTP.FORBIDDEN_403
                "Not authorized to see this resource" -> MutationResult.ERROR.HTTP.NOT_AUTHORIZED
                else -> MutationResult.ERROR.HTTP.UNKNOWN
            }
        }

    }

    override suspend fun generateMealPlan(databaseId: String): MutationResult {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .mutation(GenerateMealPlanMutation())
            .execute()

        if (result.data != null) {
            return MutationResult.SUCCESS()
        } else {
            val errorMessage = result.errors!![0].message
            when (errorMessage) {
                "400: Bad Request" -> return MutationResult.ERROR.HTTP.BAD_REQUEST_400
                "403: Forbidden" -> return MutationResult.ERROR.HTTP.FORBIDDEN_403
                "Not authorized to see this resource" -> return MutationResult.ERROR.HTTP.NOT_AUTHORIZED
                else -> return MutationResult.ERROR.HTTP.UNKNOWN
            }
        }

    }

    override suspend fun getUserHydration(databaseId: String, date: String): HydrationInfo? {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .query(GetHydrationQuery(date))
            .execute()

        if (result.data != null) {

            val hydrationJsonObject = result.data?.hydration?.get(0) ?: return null

            return HydrationInfo(
                    hydrationJsonObject.isToday ?: false,
                    hydrationJsonObject.quantity ?: 0,
                    hydrationJsonObject.date as String? ?: "",
                    hydrationJsonObject.goal ?: 0
            )

        } else {
            return null
        }

    }

    override suspend fun getUserMealPlan(databaseId: String): List<Meal>? {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .query(GetMealPlanQuery())
            .execute()

        if (result.data != null) {

            val mealsJSONArray = JSONObject(result.data!!.toJson())
                .getJSONArray("mealPlan")
                .getJSONObject(0)
                .getJSONArray("meals")

            val mealsList = ArrayList<Meal>()

            for (i in 0 until mealsJSONArray.length()) {
                val singleMeal = mealsJSONArray.getJSONObject(i)
                mealsList.add(
                    Meal(
                        MealType.valueOf(
                            singleMeal.getString("meal").uppercase(Locale.getDefault())
                        ),
                        RecipeBasic(
                            singleMeal.getJSONObject("recipe").getString("id"),
                            singleMeal.getJSONObject("recipe").getString("databaseId"),
                            singleMeal.getJSONObject("recipe").getString("name"),
                            singleMeal.getJSONObject("recipe").getString("mainImage"),
                        )
                    )
                )
            }

            return mealsList

        } else {
            return null
        }

    }

    override suspend fun getPopularRecipes(): List<RecipeBasic>? {

        val result = NetworkService.getInstance()!!.getApolloClientWithAuthToken()
            .query(GetPopularRecipesQuery())
            .execute()

        if (result.data != null) {

            val recipesJSONArray = JSONObject(result.data!!.toJson())
                .getJSONObject("popularRecipes")
                .getJSONArray("edges")

            val recipesList = ArrayList<RecipeBasic>()

            for (i in 0 until recipesJSONArray.length()) {
                val singleRecipe = recipesJSONArray.getJSONObject(i).getJSONObject("node")
                recipesList.add(
                    RecipeBasic(
                        singleRecipe.getString("id"),
                        singleRecipe.getString("databaseId"),
                        singleRecipe.getString("name"),
                        singleRecipe.getString("mainImage")
                    )
                )
            }

            return recipesList

        } else {
            return null
        }

    }

    override suspend fun getRecipesByIngredients(ingredientsList: List<String>): List<RecipeBasic>? {

        val result = NetworkService.getInstance()!!.getApolloClientWithAuthToken()
            .query(GetRecipesByIngredientsQuery(ingredientsList))
            .execute()

        if (result.data != null) {

            val recipesJSONArray = JSONObject(result.data!!.toJson())
                .getJSONObject("searchRecipesByIngredients")
                .getJSONArray("edges")

            val recipesList = ArrayList<RecipeBasic>()

            for (i in 0 until recipesJSONArray.length()) {
                val singleRecipe = recipesJSONArray.getJSONObject(i).getJSONObject("node")
                recipesList.add(
                    RecipeBasic(
                        singleRecipe.getString("id"),
                        singleRecipe.getString("databaseId"),
                        singleRecipe.getString("name"),
                        singleRecipe.getString("mainImage")
                    )
                )
            }

            return recipesList

        } else {
            return null
        }

    }

    override suspend fun getRecipeByID(id: String): RecipeFull? {

        val result = NetworkService.getInstance()!!.getApolloClientWithAuthToken()
            .query(GetRecipeByIDQuery(id))
            .execute()

        if (result.data != null) {

            val jsonObject = JSONObject(result.data!!.toJson()).getJSONObject("recipe")

            return RecipeFull(
                id,
                jsonObject.getString("databaseId"),
                jsonObject.getString("name"),
                jsonObject.getString("mainImage"),
                jsonObject.getString("totalTime"),
                linesFromJsonArray(jsonObject.getJSONArray("ingredientLines")),
                linesFromJsonArray(jsonObject.getJSONArray("instructions"))
            )

        } else {
            return null
        }
    }

    private fun linesFromJsonArray(jsonArray: JSONArray): List<String> {
        val list = ArrayList<String>()

        for (i in 0 until jsonArray.length()) list.add(jsonArray.getString(i))

        return list
    }

    override suspend fun getUsers(): List<UserData>? {

        val result = NetworkService.getInstance()!!.getApolloClientWithAuthToken()
            .query(GetUsersQuery())
            .execute()

        if (result.data != null) {

            val list = ArrayList<UserData>()

            val usersJsonArray = JSONObject(result.data!!.toJson())
                .getJSONObject("users")
                .getJSONArray("edges")

            for (i in 0 until usersJsonArray.length()) {
                val singleUser = usersJsonArray.getJSONObject(i).getJSONObject("node")
                list.add(
                    UserData(
                        singleUser.getString("id"),
                        singleUser.getString("databaseId"),
                        singleUser.getString("name"),
                        singleUser.getString("email"),
                        null, null, null, null, null, null, null, null
                    )
                )
            }

            return list
        } else {
            return null
        }
    }

    override suspend fun removeUserMealPlan(databaseId: String): MutationResult {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .mutation(RemoveMealPlanMutation())
            .execute()

        if (result.data != null) {
            return MutationResult.SUCCESS()
        } else {
            val errorMessage = result.errors!![0].message
            when (errorMessage) {
                "400: Bad Request" -> return MutationResult.ERROR.HTTP.BAD_REQUEST_400
                "403: Forbidden" -> return MutationResult.ERROR.HTTP.FORBIDDEN_403
                "Not authorized to see this resource" -> return MutationResult.ERROR.HTTP.NOT_AUTHORIZED
                else -> return MutationResult.ERROR.HTTP.UNKNOWN
            }
        }
    }

    override suspend fun updateUserHydration(databaseId: String, date: String): MutationResult {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .mutation(UpdateHydrationMutation(date))
            .execute()

        if (result.data != null) {
            return MutationResult.SUCCESS()
        } else {
            val errorMessage = result.errors!![0].message
            when (errorMessage) {
                "400: Bad Request" -> return MutationResult.ERROR.HTTP.BAD_REQUEST_400
                "403: Forbidden" -> return MutationResult.ERROR.HTTP.FORBIDDEN_403
                "Not authorized to see this resource" -> return MutationResult.ERROR.HTTP.NOT_AUTHORIZED
                else -> return MutationResult.ERROR.HTTP.UNKNOWN
            }
        }
    }

    override suspend fun updateMealPlan(databaseId: String, calories: Int): MutationResult {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .mutation(UpdateMealPlanSettingsMutation(Optional.present(calories)))
            .execute()

        if (result.data != null) {
            return MutationResult.SUCCESS()
        } else {
            val errorMessage = result.errors!![0].message
            when (errorMessage) {
                "400: Bad Request" -> return MutationResult.ERROR.HTTP.BAD_REQUEST_400
                "403: Forbidden" -> return MutationResult.ERROR.HTTP.FORBIDDEN_403
                "Not authorized to see this resource" -> return MutationResult.ERROR.HTTP.NOT_AUTHORIZED
                else -> return MutationResult.ERROR.HTTP.UNKNOWN
            }
        }
    }

    override suspend fun setProfileMacroGoals(
        databaseId: String,
        gender: UserGender,
        birthDate: String,
        height: Double,
        weight: Double
    ): MutationResult {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .mutation(
                SetProfileMacroGoalsMutation(
                    true,
                    if (gender == UserGender.MALE) BiologicalSex.MALE else BiologicalSex.FEMALE,
//                SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(Date())
                    birthDate,
                    height,
                    weight,
                    WeeklyWeightGoal.MAINTAIN,
                    ActivityLevel.NOT_ACTIVE
                )
            )
            .execute()

        if (result.data != null) {
            return MutationResult.SUCCESS()
        } else {
            val errorMessage = result.errors!![0].message
            when (errorMessage) {
                "400: Bad Request" -> return MutationResult.ERROR.HTTP.BAD_REQUEST_400
                "403: Forbidden" -> return MutationResult.ERROR.HTTP.FORBIDDEN_403
                "Not authorized to see this resource" -> return MutationResult.ERROR.HTTP.NOT_AUTHORIZED
                else -> return MutationResult.ERROR.HTTP.UNKNOWN
            }
        }
    }

    override suspend fun getUserProfile(databaseId: String): UserData? {

        val profileWithoutName =
            NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
                .query(GetUserProfileQuery())
                .execute()
                .data?.myProfile

        if (profileWithoutName == null) return null
        else {

            val usersWithNames = getUsers() ?: return null

            val userEmail = profileWithoutName.email
            var userName: String? = null

            for (user in usersWithNames) {
                if (userEmail.equals(user.email)) {
                    userName = user.name
                    break
                }
            }

            if (userName == null) return null

            return UserData(
                profileWithoutName.id as String,
                databaseId,
                userName,
                userEmail,
                profileWithoutName.birthdate as String?,
                if (profileWithoutName.biologicalSex == BiologicalSex.MALE) UserGender.MALE else UserGender.FEMALE,
                ActivityLevel.NOT_ACTIVE.toString(),
                profileWithoutName.startingWeight?.toInt(),
                profileWithoutName.height,
                profileWithoutName.goalsOn,
                profileWithoutName.dailyCaloricIntakeGoal,
                profileWithoutName.restrictions
                    ?.stream()
                    ?.map {
                        it -> Restriction(it!!.id, it.name!!)
                    }
                    ?.toList()
            )
        }
    }

    override suspend fun addUserRestrictions(
        databaseId: String,
        restrictions: List<String>
    ): MutationResult {

        val result = NetworkService.getInstance()!!.getApolloClientWithUserID(databaseId)
            .mutation(AddUserRestrictionsMutation(Optional.present(restrictions)))
            .execute()

        if (result.data != null) {
            return MutationResult.SUCCESS()
        } else {
            val errorMessage = result.errors!![0].message
            when (errorMessage) {
                "400: Bad Request" -> return MutationResult.ERROR.HTTP.BAD_REQUEST_400
                "403: Forbidden" -> return MutationResult.ERROR.HTTP.FORBIDDEN_403
                "Not authorized to see this resource" -> return MutationResult.ERROR.HTTP.NOT_AUTHORIZED
                else -> return MutationResult.ERROR.HTTP.UNKNOWN
            }
        }
    }

    override suspend fun getAllRestrictions(): List<Restriction>? {

        val result = NetworkService.getInstance()!!.getApolloClientWithAuthToken()
            .query(GetAllRestrictionsQuery())
            .execute()

        if (result.data != null) {

            val list = ArrayList<Restriction>()

            val restrictionsJsonArray = JSONObject(result.data!!.toJson())
                .getJSONObject("restrictions")
                .getJSONArray("edges")

            for (i in 0 until restrictionsJsonArray.length()) {
                val singleRestriction = restrictionsJsonArray.getJSONObject(i).getJSONObject("node")
                list.add(
                    Restriction(
                        singleRestriction.getString("id"),
                        singleRestriction.getString("name")
                    )
                )
            }

            return list
        } else {
            return null
        }
    }

    private suspend fun signInFirebaseWithEmail(
        email: String,
        password: String
    ): AuthResult? {
        return try {
            withContext(Dispatchers.IO) {
                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    .await()
            }
        } catch (e: Exception) {
            null
        }
    }
}