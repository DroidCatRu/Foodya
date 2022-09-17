package ru.droidcat.core_network_impl

import android.util.Log
import com.google.firebase.auth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.droidcat.core_network_api.MutationResult
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.core_network_api.users.UserData
import ru.droidcat.core_network_api.users.UserGender
import java.lang.Exception
import java.time.LocalDate
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
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

        //TODO: Add suggestic auth and write suggestic userID to firebase

        return if (firebaseAuthResult?.user != null) {
            MutationResult.SUCCESS
        } else {
            MutationResult.ERROR.AUTH.UNKNOWN
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

        return if (firebaseAuthResult?.user != null) {
            MutationResult.SUCCESS
        } else {
            MutationResult.ERROR.AUTH.UNKNOWN
        }
    }

    override suspend fun setUserParameters(
        weight: Int,
        height: Int,
        birthDate: LocalDate,
        gender: UserGender
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