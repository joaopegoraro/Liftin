package xyz.joaophp.liftin.data.services.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.AuthCallback
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure

class AuthServiceImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthService {

    override fun getCurrentUser(): Either<Failure, User> {
        val user = firebaseAuth.currentUser ?: return Error(AuthFailure.NoCurrentUser)
        return Success(User(user.uid))
    }

    override fun register(email: String, password: String, cb: AuthCallback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> handleTask(task, cb) }
    }

    override fun signIn(email: String, password: String, cb: AuthCallback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> handleTask(task, cb) }
    }

    override fun signInAnonymously(cb: AuthCallback) {
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener { task -> handleTask(task, cb) }
    }

    override fun signOut(): Either<Failure, Unit> {
        try {
            firebaseAuth.signOut().run {
                return if (firebaseAuth.currentUser == null) Success(Unit)
                else Error(AuthFailure.CantSignOut())
            }
        } catch (e: Exception) {
            return Error(AuthFailure.CantSignOut(e))
        }
    }

    private fun handleTask(task: Task<AuthResult>, cb: AuthCallback) {
        if (task.isSuccessful) {
            cb?.invoke(getCurrentUser())
        } else {
            val failure = getFailure(task.exception)
            cb?.invoke(Error(failure))
        }
    }

    private fun getFailure(e: Exception?): AuthFailure {
        return when (e) {
            is FirebaseAuthInvalidCredentialsException -> AuthFailure.InvalidCredentials
            is FirebaseAuthInvalidUserException -> AuthFailure.InvalidUser
            is FirebaseAuthUserCollisionException -> AuthFailure.EmailTaken
            is FirebaseAuthWeakPasswordException -> AuthFailure.WeakPassword(e.reason)
            else -> AuthFailure.Unknown(e)
        }
    }
}