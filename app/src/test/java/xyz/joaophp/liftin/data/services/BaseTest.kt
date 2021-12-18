package xyz.joaophp.liftin.data.services

import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

abstract class BaseTest {

    protected var testState = TestState.UNDEF

    protected enum class TestState {
        SUCCESSFUL, FAILED, UNDEF
    }

    protected fun <T> Either<Failure, T>.mockFold() {
        this.fold(
            ifError = { testState = TestState.FAILED },
            ifSuccess = { testState = TestState.SUCCESSFUL }
        )
    }
}