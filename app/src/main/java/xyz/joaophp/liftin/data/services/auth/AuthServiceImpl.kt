package xyz.joaophp.liftin.data.services.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.*

class AuthServiceImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthService {

    override fun getCurrentUser(): Either<Failure, User> {
        try {
            val user = firebaseAuth.currentUser ?: return Error(UserNotFoundFailure)
            return Success(User(user.uid))
        } catch (e: Exception) {
            return Error(ExceptionFailure(e))
        }
    }

    override fun register(email: String, password: String, cb: AuthCallback) {
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> handleTask(task, RegisterFailure, cb) }
        } catch (e: Exception) {
            cb?.invoke(Error(ExceptionFailure(e)))
        }
    }

    override fun signIn(email: String, password: String, cb: AuthCallback) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task -> handleTask(task, SignInFailure, cb) }
        } catch (e: Exception) {
            cb?.invoke(Error(ExceptionFailure(e)))
        }
    }

    override fun signInAnonymously(cb: AuthCallback) {
        try {
            firebaseAuth.signInAnonymously()
                .addOnCompleteListener { task -> handleTask(task, SignInFailure, cb) }
        } catch (e: Exception) {
            cb?.invoke(Error(ExceptionFailure(e)))
        }
    }

    override fun signOut(): Either<Failure, Unit> {
        try {
            firebaseAuth.signOut().run {
                return if (firebaseAuth.currentUser == null) Success(Unit)
                else Error(SignOutFailure)
            }
        } catch (e: Exception) {
            return Error(ExceptionFailure(e))
        }
    }

    private fun handleTask(task: Task<AuthResult>, failure: Failure, cb: AuthCallback) {
        try {
            if (task.isSuccessful) cb?.invoke(getCurrentUser())
            else cb?.invoke(Error(failure))
        } catch (e: Exception) {
            cb?.invoke(Error(ExceptionFailure(e)))
        }
    }


}