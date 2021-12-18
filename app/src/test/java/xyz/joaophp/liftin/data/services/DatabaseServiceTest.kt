package xyz.joaophp.liftin.data.services

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.services.database.DatabaseService
import xyz.joaophp.liftin.data.services.database.DatabaseServiceImpl
import xyz.joaophp.liftin.utils.DatabaseCallback
import xyz.joaophp.liftin.utils.DatabaseGetCallback

class DatabaseServiceTest : BaseTest() {

    private lateinit var dbService: DatabaseService

    // Mock Tasks
    private val slot = slot<OnCompleteListener<Void?>>()
    private val getSlot = slot<OnCompleteListener<DocumentSnapshot>>()
    private val successTask = mockk<Task<Void?>>()
    private val successGetTask = mockk<Task<DocumentSnapshot>>()
    private val failureTask = mockk<Task<Void?>>()
    private val failureGetTask = mockk<Task<DocumentSnapshot>>()

    // Model to be passed to the methods
    private val user = User(uid)

    // Mock FirebaseFirestore
    private val mockedFirestore = mockk<FirebaseFirestore>()

    // Mock Document
    private val mockedSnapshot = mockk<DocumentSnapshot>()
    private val mockedDocumentRef = mockk<DocumentReference>()

    // Mock Callbacks
    private val databaseStateCallback: DatabaseCallback = { it.mockFold() }
    private val databaseGetStateCallback: DatabaseGetCallback = { it.mockFold() }

    // Constants
    companion object {
        private const val uid = "12345"
        private const val path = "users/$uid"
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

        // Set up successful get task mock
        every { successGetTask.isSuccessful } returns true
        every { successGetTask.result.data } returns null
        every { successGetTask.addOnCompleteListener(capture(getSlot)) } answers {
            getSlot.captured.onComplete(successGetTask)
            successGetTask
        }

        // Set up failed get task mock
        every { failureGetTask.isSuccessful } returns false
        every { failureGetTask.exception } returns Exception()
        every { failureGetTask.addOnCompleteListener(capture(getSlot)) } answers {
            getSlot.captured.onComplete(failureGetTask)
            failureGetTask
        }

        // Set up Document mock
        every { mockedFirestore.document(path) } returns mockedDocumentRef
        every { mockedSnapshot.data } returns user.toMap()

        dbService = DatabaseServiceImpl(mockedFirestore)
    }

    @Test
    fun setFailure_test() {
        every { mockedDocumentRef.set(user.toMap()) } returns failureTask

        dbService.set(user, path, databaseStateCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun setSuccess_test() {
        every { mockedDocumentRef.set(user.toMap()) } returns successTask

        dbService.set(user, path, databaseStateCallback)
        assert(testState == TestState.SUCCESSFUL)
    }

    @Test
    fun getFailure_test() {
        every { mockedDocumentRef.get() } returns failureGetTask

        dbService.get(path, databaseGetStateCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun getSuccess_test() {
        every { mockedDocumentRef.get() } returns successGetTask

        dbService.get(path, databaseGetStateCallback)
        assert(testState == TestState.SUCCESSFUL)
    }

    @Test
    fun deleteFailure_test() {
        every { mockedDocumentRef.delete() } returns failureTask

        dbService.delete(user, path, databaseStateCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun deleteSuccess_test() {
        every { mockedDocumentRef.delete() } returns successTask

        dbService.delete(user, path, databaseStateCallback)
        assert(testState == TestState.SUCCESSFUL)
    }

}