package xyz.joaophp.liftin.utils

interface Failure

class UnknownFailure : Failure
class UserNotFoundFailure : Failure
class SignInFailure : Failure
class SignOutFailure : Failure