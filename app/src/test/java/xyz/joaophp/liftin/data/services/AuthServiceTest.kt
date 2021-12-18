package xyz.joaophp.liftin.data.services

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.services.auth.AuthService
import xyz.joaophp.liftin.data.services.auth.AuthServiceImpl
import xyz.joaophp.liftin.utils.AuthCallback
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success


class AuthServiceTest : BaseTest() {

    private lateinit var authService: AuthService

    // Mock Task
    private val slot = slot<OnCompleteListener<AuthResult>>()
    private val successTask = mockk<Task<AuthResult>>()
    private val failureTask = mockk<Task<AuthResult>>()

    // Mock FirebaseAuth
    private val mockedFirebaseAuth = mockk<FirebaseAuth>()
    private val mockedFirebaseUser = mockk<FirebaseUser>()

    // MockCallback
    private val mockedAuthCallback: AuthCallback = { it.mockFold() }

    // Constants
    companion object {
        private const val userUid = "12345ABIDE"
        private const val email = "cool@cool.com"
        private const val password = "123456"
    }

    @Before
    fun setUp() {

        // Set up successful task mock
        every { successTask.isSuccessful } returns true
        every { successTask.addOnCompleteListener(capture(slot)) } answers {
            slot.captured.onComplete(successTask)
            successTask
        }

        // Set up failed task mock
        every { failureTask.isSuccessful } returns false
        every { failureTask.exception } returns Exception()
        every { failureTask.addOnCompleteListener(capture(slot)) } answers {
            slot.captured.onComplete(failureTask)
            failureTask
        }

        // Setup FirebaseAuth mock
        every { mockedFirebaseAuth.currentUser } returns mockedFirebaseUser
        every { mockedFirebaseUser.uid } returns userUid
        every { mockedFirebaseAuth.signOut() } returns Unit

        authService = AuthServiceImpl(mockedFirebaseAuth)
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

    @Test
    fun registerFailure_test() {
        every {
            mockedFirebaseAuth.createUserWithEmailAndPassword(email, password)
        } returns failureTask


        authService.register(email, password, mockedAuthCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun registerSuccess_test() {
        every {
            mockedFirebaseAuth.createUserWithEmailAndPassword(email, password)
        } returns successTask


        authService.register(email, password, mockedAuthCallback)
        assert(testState == TestState.SUCCESSFUL)
    }

    @Test
    fun signInSuccess_test() {
        every {
            mockedFirebaseAuth.signInWithEmailAndPassword(email, password)
        } returns successTask

        authService.signIn(email, password, mockedAuthCallback)
        assert(testState == TestState.SUCCESSFUL)
    }

    @Test
    fun signInFailure_test() {
        every {
            mockedFirebaseAuth.signInWithEmailAndPassword(email, password)
        } returns failureTask

        authService.signIn(email, password, mockedAuthCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun signInAnonymouslyFailure_test() {
        every {
            mockedFirebaseAuth.signInAnonymously()
        } returns failureTask

        authService.signInAnonymously(mockedAuthCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun signInAnonymouslySuccess_test() {
        every {
            mockedFirebaseAuth.signInAnonymously()
        } returns successTask

        authService.signInAnonymously(mockedAuthCallback)
        assert(testState == TestState.SUCCESSFUL)
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