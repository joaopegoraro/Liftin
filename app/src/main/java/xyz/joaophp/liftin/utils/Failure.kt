package xyz.joaophp.liftin.utils

interface Failure

class TaskNotCompletedFailure : Failure
class UserNotFoundFailure : Failure
class SignInFailure : Failure