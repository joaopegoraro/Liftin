package xyz.joaophp.liftin.data.services

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.services.database.DatabaseService
import xyz.joaophp.liftin.data.services.database.DatabaseServiceImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Helpers
import xyz.joaophp.liftin.utils.Success

class DatabaseServiceTest {

    private lateinit var dbService: DatabaseService

    // Model to be passed to the methods
    private val user = User(uid)

    // Mock FirebaseFirestore
    private val mockedFirestore = mockk<FirebaseFirestore>()
    private val mockedSnapshot = mockk<DocumentSnapshot>()
    private val mockedGetAllSnapshot = mockk<QuerySnapshot>()

    // Mock Exception
    private val exception = Exception()

    // Constants
    companion object {
        private const val uid = "12345"
        private const val path = "users/$uid"
    }

    @Before
    fun setUp() {
        // Mock await() method
        mockkStatic("kotlinx.coroutines.tasks.TasksKt") // IMPORTANT!
        every { mockedSnapshot.data } returns mapOf()
        dbService = DatabaseServiceImpl(mockedFirestore)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun setFailure_test() = runTest {
        coEvery { mockedFirestore.document(path).set(user.toMap()).await() } throws exception

        val result = dbService.set(user, path)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun setSuccess_test() = runTest {
        coEvery {
            mockedFirestore.document(path).set(user.toMap()).await()
        } returns Helpers.makeVoid()

        val result = dbService.set(user, path)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getFailure_test() = runTest {
        coEvery { mockedFirestore.document(path).get().await() } throws exception

        val result = dbService.get(path)
        assert(result is Error)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getSuccess_test() = runTest {
        coEvery { mockedFirestore.document(path).get().await() } returns mockedSnapshot

        val result = dbService.get(path)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAllFailure_test() = runTest {
        coEvery { mockedFirestore.collection(path).get().await() } throws exception

        val result = dbService.getAll(path)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAllSuccess_test() = runTest {
        coEvery { mockedFirestore.collection(path).get().await() } returns mockedGetAllSnapshot
        every { mockedGetAllSnapshot.documents } returns listOf(mockedSnapshot)

        val result = dbService.getAll(path)
        assert(result is Success)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun deleteFailure_test() = runTest {
        coEvery { mockedFirestore.document(path).delete().await() } throws exception

        val result = dbService.delete(user, path)
        assert(result is Error)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun deleteSuccess_test() = runTest {
        coEvery { mockedFirestore.document(path).delete().await() } returns Helpers.makeVoid()

        val result = dbService.delete(user, path)
        assert(result is Success)
    }
}