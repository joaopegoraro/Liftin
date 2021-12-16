package xyz.joaophp.liftin.data.services

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
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
import xyz.joaophp.liftin.utils.Helpers

class DatabaseServiceTest : BaseTest() {

    private lateinit var dbService: DatabaseService

    // Mock Tasks
    private val successTask = mockk<Task<Void?>>()
    private val successfulSlot = slot<OnSuccessListener<Void?>>()
    private val successfulGetSlot = slot<OnSuccessListener<DocumentSnapshot>>()
    private val failedSlot = slot<OnFailureListener>()
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
        every { successTask.addOnSuccessListener(capture(successfulSlot)) } answers {
            // Only way that I found to be able to pass Void as a parameter was using the
            // makeVoid() method
            successfulSlot.captured.onSuccess(Helpers.makeVoid())
            successTask
        }
        every { successTask.addOnFailureListener(capture(failedSlot)) } answers {
            successTask
        }

        // Set up failed task mock
        every { failureTask.isSuccessful } returns false
        every { failureTask.addOnSuccessListener(capture(successfulSlot)) } answers {
            failureTask
        }
        every { failureTask.addOnFailureListener(capture(failedSlot)) } answers {
            failedSlot.captured.onFailure(Exception())
            failureTask
        }

        // Set up successful get task mock
        every { successGetTask.isSuccessful } returns true
        every { successGetTask.addOnSuccessListener(capture(successfulGetSlot)) } answers {
            successfulGetSlot.captured.onSuccess(mockedSnapshot)
            successGetTask
        }
        every { successGetTask.addOnFailureListener(capture(failedSlot)) } answers {
            successGetTask
        }

        // Set up failed get task mock
        every { failureGetTask.isSuccessful } returns false
        every { failureGetTask.addOnSuccessListener(capture(successfulGetSlot)) } answers {
            successfulGetSlot.captured.onSuccess(mockedSnapshot)
            failureGetTask
        }
        every { failureGetTask.addOnFailureListener(capture(failedSlot)) } answers {
            failedSlot.captured.onFailure(Exception())
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