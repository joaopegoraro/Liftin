package xyz.joaophp.liftin.data.services.login

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.*

class LoginServiceImpl : LoginService {

    // Instantiate FirebaseAuth service
    private val firebaseAuth = FirebaseAuth.getInstance();

    override fun getCurrentUser(): Either<Failure, User> {
        val user = firebaseAuth.currentUser ?: return Error(UserNotFoundFailure())
        return Success(User(user.uid))
    }

    override fun signIn(email: String, password: String, cb: (Either<Failure, User>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> handleTask(task, cb) }
    }

    override fun signInAnonymously(cb: (Either<Failure, User>) -> Unit) {
        firebaseAuth.signInAnonymously()
            .addOnCompleteListener { task -> handleTask(task, cb) }
    }

    override fun signOut() = firebaseAuth.signOut()

    private fun handleTask(task: Task<AuthResult>, cb: (Either<Failure, User>) -> Unit) {
        if (task.isSuccessful) {
            val user = firebaseAuth.currentUser ?: return cb(Error(UserNotFoundFailure()))
            cb(Success(User(user.uid)))
        } else {
            cb(Error(SignInFailure()))
        }
    }


}