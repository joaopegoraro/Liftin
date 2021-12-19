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
import xyz.joaophp.liftin.usecases.auth.RegisterUseCase
import xyz.joaophp.liftin.usecases.auth.RegisterUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure

class RegisterUseCaseTest {

    private lateinit var registerUseCase: RegisterUseCase

    // Mock UserRepository
    private val mockUserRepository = mockk<UserRepository>()

    // Mock User
    private val user = User(uid)

    // Mock errors
    private val retrieveError = Error<Failure, Boolean>(AuthFailure.CantRetrieveUser(Exception()))
    private val registerError = Error<Failure, User>(AuthFailure.BadEmail)

    // Constants
    companion object {
        private const val uid = "uid"
        private const val email = "abcde@abcde.com"
        private const val password = "123456"
    }

    @Before
    fun setUp() {
        registerUseCase = RegisterUseCaseImpl(mockUserRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun isUserLoggedFailure_test() = runTest {
        every { mockUserRepository.isUserLoggedIn() } returns retrieveError

        val result = registerUseCase(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun userAlreadyLogged_test() = runTest {
        every { mockUserRepository.isUserLoggedIn() } returns Success(true)

        val result = registerUseCase(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun registerFailure_test() = runTest {
        every { mockUserRepository.isUserLoggedIn() } returns Success(false)
        coEvery { mockUserRepository.register(email, password) } returns registerError

        val result = registerUseCase(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun registerSuccess_test() = runTest {
        every { mockUserRepository.isUserLoggedIn() } returns Success(false)
        coEvery { mockUserRepository.register(email, password) } returns Success(user)

        val result = registerUseCase(email, password)
        assert(result is Success)
    }
}