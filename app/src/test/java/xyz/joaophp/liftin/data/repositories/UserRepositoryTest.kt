package xyz.joaophp.liftin.data.repositories

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.user.UserRepository
import xyz.joaophp.liftin.data.repositories.user.UserRepositoryImpl
import xyz.joaophp.liftin.data.services.auth.AuthService
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository

    // Mock AuthService
    private val mockAuthService = mockk<AuthService>()

    // Mock user
    private val user = User(uid)

    // Mock error
    private val error = Error<Failure, User>(AuthFailure.NoCurrentUser)
    private val isUserLoggedError = Error<Failure, User>(AuthFailure.CantRetrieveUser(Exception()))
    private val signOutError = Error<Failure, Unit>(AuthFailure.NoCurrentUser)

    // Constants
    companion object {
        private const val uid = "uid"
        private const val email = "abcd@abcd.com"
        private const val password = "123456"
    }

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(mockAuthService)
    }

    @Test
    fun getCurrentUserFailure_test() {
        every { mockAuthService.getCurrentUser() } returns error

        val result = userRepository.getCurrentUser()
        assert(result is Error)
    }

    @Test
    fun getCurrentUserNoUserFailure_test() {
        every { mockAuthService.getCurrentUser() } returns error

        val result = userRepository.getCurrentUser()
        assert(result is Error)
    }

    @Test
    fun getCurrentUserSuccess_test() {
        every { mockAuthService.getCurrentUser() } returns Success(user)

        val result = userRepository.getCurrentUser()
        assert(result is Success)
    }

    @Test
    fun isUserLoggedInFailure_test() {
        every { mockAuthService.getCurrentUser() } returns isUserLoggedError

        val result = userRepository.isUserLoggedIn()
        assert(result is Error)
    }

    @Test
    fun isUserLoggedInSuccessFalse_test() {
        every { mockAuthService.getCurrentUser() } returns error

        val result = userRepository.isUserLoggedIn()
        assert(result is Success)
    }

    @Test
    fun isUserLoggedInSuccessTrue_test() {
        every { mockAuthService.getCurrentUser() } returns Success(user)

        val result = userRepository.isUserLoggedIn()
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun registerFailure_test() = runTest {
        coEvery { mockAuthService.register(email, password) } returns error

        val result = userRepository.register(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun registerSuccess_test() = runTest {
        coEvery { mockAuthService.register(email, password) } returns Success(user)

        val result = userRepository.register(email, password)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun signInFailure_test() = runTest {
        coEvery { mockAuthService.signIn(email, password) } returns error

        val result = userRepository.signIn(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun signInSuccess_test() = runTest {
        coEvery { mockAuthService.signIn(email, password) } returns Success(user)

        val result = userRepository.signIn(email, password)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun signInAnonymouslyFailure_test() = runTest {
        coEvery { mockAuthService.signInAnonymously() } returns error

        val result = userRepository.signInAnonymously()
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun signInAnonymouslySuccess_test() = runTest {
        coEvery { mockAuthService.signInAnonymously() } returns Success(user)

        val result = userRepository.signInAnonymously()
        assert(result is Success)
    }

    @Test
    fun signOutFailure_test() {
        every { mockAuthService.signOut() } returns signOutError

        val result = userRepository.signOut()
        assert(result is Error)
    }

    @Test
    fun signOutSuccess_test() {
        every { mockAuthService.signOut() } returns Success(Unit)

        val result = userRepository.signOut()
        assert(result is Success)
    }
}
