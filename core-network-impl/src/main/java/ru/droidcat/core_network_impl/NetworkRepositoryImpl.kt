package ru.droidcat.core_network_impl

import android.util.Log
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.droidcat.core_network_api.MutationResult
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.core_network_api.hydration.HydrationInfo
import ru.droidcat.core_network_api.meal.Meal
import ru.droidcat.core_network_api.recipe.RecipeBasic
import ru.droidcat.core_network_api.recipe.RecipeFull
import ru.droidcat.core_network_api.users.UserData
import ru.droidcat.core_network_api.users.UserGender
import java.lang.Exception
import java.time.LocalDate
import javax.inject.Inject

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

        firebaseAuth.createUserWithEmailAndPassword(email, password)

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

        //TODO: Add suggestic auth and write suggestic userID to firebase

//        firebaseDb.getReference("users").child(user.uid).setValue()
//
         return MutationResult.SUCCESS
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

        return if (firebaseAuthResult?.user != null) {
            MutationResult.SUCCESS
        } else {
            MutationResult.ERROR.AUTH.UNKNOWN
        }
    }

    override suspend fun removeUserProfile(): MutationResult {
        TODO("Not yet implemented")
    }

    override suspend fun generateMealPlan(): MutationResult {
        TODO("Not yet implemented")
    }

    override suspend fun getUserHydration(date: String): HydrationInfo {
        TODO("Not yet implemented")
    }

    override suspend fun getUserMealPlan(): List<Meal> {
        TODO("Not yet implemented")
    }

    override suspend fun getPopularRecipes(): List<RecipeBasic> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipesByIngredients(): List<RecipeBasic> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeByID(id: String): RecipeFull {
        TODO("Not yet implemented")
    }

    override suspend fun getUsers(): List<UserData> {
        TODO("Not yet implemented")
    }

    override suspend fun removeUserMealPlan(): MutationResult {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserHydration(date: String): MutationResult {
        TODO("Not yet implemented")
    }

    override suspend fun updateMealPlan(calories: Int): MutationResult {
        TODO("Not yet implemented")
    }

    override suspend fun setProfileMacroGoals(
        gender: UserGender,
        birthDate: String,
        height: Int,
        weight: Int
    ): MutationResult {
        TODO("Not yet implemented")
    }

    override suspend fun getUserProfile(): UserData? {
        TODO("Not yet implemented")
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