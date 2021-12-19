package xyz.joaophp.liftin.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.user.UserRepository
import xyz.joaophp.liftin.usecases.auth.GetUserUseCase
import xyz.joaophp.liftin.usecases.auth.GetUserUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.AuthFailure

class GetUserUseCaseTest {

    private lateinit var getUserUseCase: GetUserUseCase

    // Mock UserRepository
    private val mockUserRepository = mockk<UserRepository>()

    // Mock user
    private val user = User(uid)

    // Constants
    companion object {
        private const val uid = "uid"
    }

    @Before
    fun setUp() {
        getUserUseCase = GetUserUseCaseImpl(mockUserRepository)
    }

    @Test
    fun getUserFailure_test() {
        every { mockUserRepository.getCurrentUser() } returns Error(AuthFailure.NoCurrentUser)

        val result = getUserUseCase()
        assert(result is Error)
    }

    @Test
    fun getUserSuccess_test() {
        every { mockUserRepository.getCurrentUser() } returns Success(user)

        val result = getUserUseCase()
        assert(result is Success)
    }
}