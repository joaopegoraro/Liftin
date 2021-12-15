package xyz.joaophp.liftin.utils

sealed class Either<E, S>(
    private val error: E? = null,
    private val success: S? = null,
) {
    abstract fun <B> fold(
        ifError: (error: E) -> B,
        ifSuccess: (success: S) -> B
    ): B

    abstract suspend fun <B> foldAsync(
        ifError: suspend (error: E) -> B,
        ifSuccess: suspend (success: S) -> B
    ): B
}

class Success<E, S>(private val value: S) : Either<E, S>(success = value) {

    override fun <B> fold(
        ifError: (error: E) -> B,
        ifSuccess: (success: S) -> B
    ) = ifSuccess(value)

    override suspend fun <B> foldAsync(
        ifError: suspend (error: E) -> B,
        ifSuccess: suspend (success: S) -> B
    ) = ifSuccess(value)
}

class Error<E, S>(private val value: E) : Either<E, S>(error = value) {
    override fun <B> fold(
        ifError: (error: E) -> B,
        ifSuccess: (success: S) -> B
    ) = ifError(value)

    override suspend fun <B> foldAsync(
        ifError: suspend (error: E) -> B,
        ifSuccess: suspend (success: S) -> B
    ) = ifError(value)
}

