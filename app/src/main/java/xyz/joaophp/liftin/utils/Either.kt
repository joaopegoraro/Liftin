package xyz.joaophp.liftin.utils

sealed class Either<L, R>(
    private val left: L? = null,
    private val right: R? = null,
) {
    abstract fun <B> fold(
        ifLeft: (left: L) -> B,
        ifRight: (right: R) -> B
    ): B

    abstract suspend fun <B> foldAsync(
        ifLeft: suspend (left: L) -> B,
        ifRight: suspend (right: R) -> B
    ): B
}

class Success<L, R>(private val value: R) : Either<L, R>(right = value) {

    override fun <B> fold(
        ifLeft: (left: L) -> B,
        ifRight: (right: R) -> B
    ) = ifRight(value)

    override suspend fun <B> foldAsync(
        ifLeft: suspend (left: L) -> B,
        ifRight: suspend (right: R) -> B
    ) = ifRight(value)
}

class Error<L, R>(private val value: L) : Either<L, R>(left = value) {
    override fun <B> fold(
        ifLeft: (left: L) -> B,
        ifRight: (right: R) -> B
    ) = ifLeft(value)

    override suspend fun <B> foldAsync(
        ifLeft: suspend (left: L) -> B,
        ifRight: suspend (right: R) -> B
    ) = ifLeft(value)
}

