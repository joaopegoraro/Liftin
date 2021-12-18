package xyz.joaophp.liftin.data.services

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.services.auth.AuthService
import xyz.joaophp.liftin.data.services.auth.AuthServiceImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success


class AuthServiceTest {

    private lateinit var authService: AuthService

    // Mock FirebaseAuth
    private val mockedFirebaseAuth = mockk<FirebaseAuth>()
    private val mockedFirebaseUser = mockk<FirebaseUser>()
    private val mockedResult = mockk<AuthResult>()

    // Constants
    companion object {
        private const val userUid = "12345ABIDE"
        private const val email = "cool@cool.com"
        private const val password = "123456"
    }

    @Before
    fun setUp() {

        // Mock await() method
        mockkStatic("kotlinx.coroutines.tasks.TasksKt") // IMPORTANT!

        // Setup FirebaseAuth mock
        every { mockedFirebaseAuth.currentUser } returns mockedFirebaseUser
        every { mockedFirebaseUser.uid } returns userUid
        every { mockedFirebaseAuth.signOut() } returns Unit

        authService = AuthServiceImpl(mockedFirebaseAuth)
    }

    @Test
    fun getCurrentUserException_test() {
        every { mockedFirebaseAuth.currentUser } throws Exception()

        val result = authService.getCurrentUser()
        assert(result is Error)
    }

    @Test
    fun getCurrentUserFailure_test() {
        every { mockedFirebaseAuth.currentUser } returns null

        val result = authService.getCurrentUser()
        assert(result is Error)
    }

    @Test
    fun getCurrentUserSuccess_test() {
        val result = authService.getCurrentUser()
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun registerFailure_test() = runTest {
        coEvery {
            mockedFirebaseAuth.createUserWithEmailAndPassword(email, password).await()
        } throws Exception()


        val result = authService.register(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun registerSuccess_test() = runTest {
        coEvery {
            mockedFirebaseAuth.createUserWithEmailAndPassword(email, password).await()
        } returns mockedResult

        val result = authService.register(email, password)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun signInFailure_test() = runTest {
        coEvery {
            mockedFirebaseAuth.signInWithEmailAndPassword(email, password).await()
        } throws Exception()

        val result = authService.signIn(email, password)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun signInSuccess_test() = runTest {
        coEvery {
            mockedFirebaseAuth.signInWithEmailAndPassword(email, password).await()
        } returns mockedResult

        val result = authService.signIn(email, password)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun signInAnonymouslyFailure_test() = runTest {
        coEvery {
            mockedFirebaseAuth.signInAnonymously().await()
        } throws Exception()

        val result = authService.signInAnonymously()
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun signInAnonymouslySuccess_test() = runTest {
        coEvery {
            mockedFirebaseAuth.signInAnonymously().await()
        } returns mockedResult

        val result = authService.signInAnonymously()
        assert(result is Success)
    }

    @Test
    fun signOutFailure_test() {
        val result = authService.signOut()
        assert(result is Error)
    }

    @Test
    fun signOutSuccess_test() {
        every { mockedFirebaseAuth.currentUser } returns null

        val result = authService.signOut()
        assert(result is Success)
    }

}