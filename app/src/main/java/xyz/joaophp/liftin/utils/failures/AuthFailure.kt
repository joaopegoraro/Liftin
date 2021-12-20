package xyz.joaophp.liftin.utils.failures

sealed class AuthFailure(e: Exception? = null) : Failure(e) {
    object EmptyCredentials : AuthFailure()
    object BadEmail : AuthFailure()
    object NoCurrentUser : AuthFailure()
    object InvalidCredentials : AuthFailure()
    object InvalidUser : AuthFailure()
    object EmailTaken : AuthFailure()
    class CantRetrieveUser(e: Exception?) : AuthFailure(e)
    class CantSignOut(e: Exception? = null) : AuthFailure(e)
    class WeakPassword(val reason: String?) : AuthFailure()
    class Unknown(e: Exception?) : AuthFailure(e)
}

