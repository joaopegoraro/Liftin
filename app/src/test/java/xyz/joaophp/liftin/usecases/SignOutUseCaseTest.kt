package xyz.joaophp.liftin.usecases

import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.repositories.user.UserRepository
import xyz.joaophp.liftin.usecases.auth.SignOutUseCase
import xyz.joaophp.liftin.usecases.auth.SignOutUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.AuthFailure

class SignOutUseCaseTest {

    private lateinit var signOutUseCase: SignOutUseCase

    // Mock UserRepository
    private val mockUserRepository = mockk<UserRepository>()

    @Before
    fun setUp() {
        signOutUseCase = SignOutUseCaseImpl(mockUserRepository)
    }

    @Test
    fun signOutFailure_test() {
        every { mockUserRepository.signOut() } returns Error(AuthFailure.CantSignOut(Exception()))

        val result = signOutUseCase()
        assert(result is Error)
    }

    @Test
    fun signOutSuccess_test() {
        every { mockUserRepository.signOut() } returns Success(Unit)

        val result = signOutUseCase()
        assert(result is Success)
    }
}