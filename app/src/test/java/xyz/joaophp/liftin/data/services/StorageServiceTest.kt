package xyz.joaophp.liftin.data.services

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.services.storage.StorageService
import xyz.joaophp.liftin.data.services.storage.StorageServiceImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Helpers
import xyz.joaophp.liftin.utils.Success

class StorageServiceTest {

    private lateinit var storageService: StorageService

    // Mock Uri
    private val mockUri = mockk<Uri>()

    // Mock FirebaseStorage
    private val mockStorage = mockk<FirebaseStorage>()
    private val mockRef = mockk<StorageReference>()
    private val imagesRef = mockk<StorageReference>()

    // Mock returns
    private val mockSnapshot = mockk<UploadTask.TaskSnapshot>()
    private val mockByteArray = byteArrayOf()


    // Constants
    companion object {
        private const val imageRefPath = "images/imagem.jpg"
        private const val ONE_MEGABYTE: Long = 1024 * 1024
    }

    @Before
    fun setUp() {

        // Mock await() method
        mockkStatic("kotlinx.coroutines.tasks.TasksKt") // IMPORTANT!

        // Setup Mock FirebaseStorage
        every { mockStorage.reference } returns mockRef
        every { mockRef.child(imageRefPath) } returns imagesRef

        storageService = StorageServiceImpl(mockStorage)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun uploadFailure_test() = runTest {
        coEvery { imagesRef.putFile(mockUri).await() } throws Exception()

        val result = storageService.upload(imageRefPath, mockUri)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun uploadSuccess_test() = runTest {
        coEvery { imagesRef.putFile(mockUri).await().uploadSessionUri } returns mockUri

        val result = storageService.upload(imageRefPath, mockUri)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun downloadFailure_test() = runTest {
        coEvery { imagesRef.getBytes(ONE_MEGABYTE) } throws Exception()

        val result = storageService.download(imageRefPath)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun downloadSuccess_test() = runTest {
        coEvery { imagesRef.getBytes(ONE_MEGABYTE).await() } returns mockByteArray

        val result = storageService.download(imageRefPath)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteFailure_test() = runTest {
        coEvery { imagesRef.delete().await() } throws Exception()

        val result = storageService.delete(imageRefPath)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteSuccess_test() = runTest {
        coEvery { imagesRef.delete().await() } returns Helpers.makeVoid()

        val result = storageService.delete(imageRefPath)
        assert(result is Success)
    }
}