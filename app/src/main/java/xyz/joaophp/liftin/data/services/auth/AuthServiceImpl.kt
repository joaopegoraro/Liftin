package xyz.joaophp.liftin.data.services.auth

import com.google.firebase.auth.*
import kotlinx.coroutines.tasks.await
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthService {

    override fun getCurrentUser(): Either<Failure, User> {
        try {
            val user = firebaseAuth.currentUser ?: return Error(AuthFailure.NoCurrentUser)
            return Success(User(user.uid))
        } catch (e: Exception) {
            return Error(AuthFailure.CantRetrieveUser(e))
        }
    }

    override suspend fun register(email: String, password: String): Either<Failure, User> {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            getCurrentUser()
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    override suspend fun signIn(email: String, password: String): Either<Failure, User> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            getCurrentUser()
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    override suspend fun signInAnonymously(): Either<Failure, User> {
        return try {
            firebaseAuth.signInAnonymously().await()
            getCurrentUser()
        } catch (e: Exception) {
            val failure = getFailure(e)
            Error(failure)
        }
    }

    override fun signOut(): Either<Failure, Unit> {
        return try {
            firebaseAuth.signOut()
            if (firebaseAuth.currentUser == null) Success(Unit)
            else Error(AuthFailure.CantSignOut())
        } catch (e: Exception) {
            Error(AuthFailure.CantSignOut(e))
        }
    }

    private fun getFailure(e: Exception?): AuthFailure {
        return if (e is FirebaseAuthException) {
            if (e.errorCode == "auth/invalid-email") return AuthFailure.BadEmail
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> AuthFailure.InvalidCredentials
                is FirebaseAuthInvalidUserException -> AuthFailure.InvalidUser
                is FirebaseAuthUserCollisionException -> AuthFailure.EmailTaken
                is FirebaseAuthWeakPasswordException -> AuthFailure.WeakPassword(e.reason)
                else -> AuthFailure.Unknown(e)
            }
        } else {
            AuthFailure.Unknown(e)
        }
    }
}