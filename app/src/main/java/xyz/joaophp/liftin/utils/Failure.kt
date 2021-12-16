package xyz.joaophp.liftin.utils

interface Failure

class UnknownFailure : Failure
class UserNotFoundFailure : Failure
class RegisterFailure : Failure
class SignInFailure : Failure
class SignOutFailure : Failure
class CreateDocumentFailure : Failure
class ReadDocumentFailure : Failure
class DocumentNotFoundFailure : Failure
class DeleteDocumentFailure : Failure
class ConversionFailure: Failure