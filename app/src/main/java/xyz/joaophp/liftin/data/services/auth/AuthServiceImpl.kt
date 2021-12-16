package xyz.joaophp.liftin.data.services.auth

import android.content.pm.SigningInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.*

class AuthServiceImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthService {

    override fun getCurrentUser(): Either<Failure, User> {
        val user = firebaseAuth.currentUser ?: return Error(UserNotFoundFailure())
        return Success(User(user.uid))
    }

    override fun register(email: String, password: String, cb: AuthCallback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> handleTask(task, RegisterFailure(), cb) }
    }

    override fun signIn(email: String, password: String, cb: AuthCallback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> handleTask(task, SignInFailure(), cb) }
    }

    override fun signInAnonymously(cb: AuthCallback) {
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener { task -> handleTask(task, SignInFailure(), cb) }
    }

    override fun signOut(): Either<Failure, Unit> {
        firebaseAuth.signOut().run {
            return if (firebaseAuth.currentUser == null) Success(Unit)
            else Error(SignOutFailure())
        }
    }

    private fun handleTask(task: Task<AuthResult>, failure: Failure, cb: AuthCallback) {
        if (task.isSuccessful) cb?.invoke(getCurrentUser())
        else cb?.invoke(Error(failure))
    }


}