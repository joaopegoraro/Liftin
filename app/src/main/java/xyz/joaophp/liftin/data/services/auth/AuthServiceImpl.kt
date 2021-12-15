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
        val user = firebaseAuth.currentUser ?: return Error(UserNotFoundFailure())
        return Success(User(user.uid))
    }

    override fun register(email: String, password: String, cb: (Either<Failure, User>) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> handleTask(task, cb) }
    }

    override fun signIn(email: String, password: String, cb: (Either<Failure, User>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> handleTask(task, cb) }
    }

    override fun signInAnonymously(cb: (Either<Failure, User>) -> Unit) {
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener { task -> handleTask(task, cb) }
    }

    override fun signOut(): Either<Failure, Unit> {
        firebaseAuth.signOut().run {
            return if (firebaseAuth.currentUser == null) Success(Unit)
            else Error(SignOutFailure())
        }
    }

    private fun handleTask(task: Task<AuthResult>, cb: (Either<Failure, User>) -> Unit) {
        if (task.isSuccessful) cb(getCurrentUser())
        else cb(Error(SignInFailure()))
    }


}