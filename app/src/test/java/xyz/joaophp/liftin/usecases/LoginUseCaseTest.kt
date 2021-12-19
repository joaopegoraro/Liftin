package xyz.joaophp.liftin.usecases

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.user.UserRepository
import xyz.joaophp.liftin.usecases.auth.LoginUseCase
import xyz.joaophp.liftin.usecases.auth.LoginUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.LoginFailure

class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase

    // Mock UserRepository
    private val mockUserRepository = mockk<UserRepository>()

    // Mock User
    private val user = User(uid)

    // Mock errors
    private val retrieveError = Error<Failure, Boolean>(AuthFailure.CantRetrieveUser(Exception()))
    private val signInError = Error<Failure, User>(AuthFailure.BadEmail)

    // Constants
    companion object {
        private const val uid = "uid"
        private const val email = "abcde@abcde.com"
        private const val password = "123456"
    }

    @Before
    fun setUp() {
        loginUseCase = LoginUseCaseImpl(mockUserRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun isUserLoggedFailure_test() = runTest {
        every { mockUserRepository.isUserLoggedIn() } returns retrieveError

        val result = loginUseCase(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun userAlreadyLogged_test() = runTest {
        every { mockUserRepository.isUserLoggedIn() } returns Success(true)

        val result = loginUseCase(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loginFailure_test() = runTest {
        every { mockUserRepository.isUserLoggedIn() } returns Success(false)
        coEvery { mockUserRepository.signIn(email, password) } returns signInError

        val result = loginUseCase(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loginSuccess_test() = runTest {
        every { mockUserRepository.isUserLoggedIn() } returns Success(false)
        coEvery { mockUserRepository.signIn(email, password) } returns Success(user)

        val result = loginUseCase(email, password)
        assert(result is Success)
    }
}