package xyz.joaophp.liftin.data.services

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.services.storage.StorageService
import xyz.joaophp.liftin.data.services.storage.StorageServiceImpl
import xyz.joaophp.liftin.utils.StorageCallback
import xyz.joaophp.liftin.utils.StorageDownloadCallback

class StorageServiceTest : BaseTest() {

    private lateinit var storageService: StorageService

    // Mock Uri
    private val mockUri = mockk<Uri>()

    // Mock Slots
    private val uploadSlot = slot<OnCompleteListener<UploadTask.TaskSnapshot>>()
    private val downloadSlot = slot<OnCompleteListener<ByteArray>>()
    private val deleteSlot = slot<OnCompleteListener<Void>>()

    // Mock download tasks
    private val successDownloadTask = mockk<Task<ByteArray>>()
    private val failureDownloadTask = mockk<Task<ByteArray>>()

    // Mock Upload tasks
    private val successUploadTask = mockk<UploadTask>()
    private val failureUploadTask = mockk<UploadTask>()

    // Mock delete tasks
    private val successDeleteTask = mockk<Task<Void>>()
    private val failureDeleteTask = mockk<Task<Void>>()

    // Mock Callbacks
    private val storageCallback: StorageCallback = { it.mockFold() }
    private val downloadCallback: StorageDownloadCallback = { it.mockFold() }

    // Mock FirebaseStorage
    private val mockStorage = mockk<FirebaseStorage>()
    private val mockRef = mockk<StorageReference>()
    private val imagesRef = mockk<StorageReference>()

    // Constants
    companion object {
        private const val imageRefPath = "images/imagem.jpg"
        private const val ONE_MEGABYTE: Long = 1024 * 1024
    }

    @Before
    fun setUp() {

        // Set up successful upload task mock
        every { successUploadTask.isSuccessful } returns true
        every { successUploadTask.addOnCompleteListener(capture(uploadSlot)) } answers {
            uploadSlot.captured.onComplete(successUploadTask)
            successUploadTask
        }

        // Set up failed upload task mock
        every { failureUploadTask.isSuccessful } returns false
        every { failureUploadTask.exception } returns Exception()
        every { failureUploadTask.addOnCompleteListener(capture(uploadSlot)) } answers {
            uploadSlot.captured.onComplete(failureUploadTask)
            failureUploadTask
        }

        // Set up successful download task mock
        every { successDownloadTask.isSuccessful } returns true
        every { successDownloadTask.result } returns byteArrayOf()
        every { successDownloadTask.addOnCompleteListener(capture(downloadSlot)) } answers {
            downloadSlot.captured.onComplete(successDownloadTask)
            successDownloadTask
        }

        // Set up failed download task mock
        every { failureDownloadTask.isSuccessful } returns false
        every { failureDownloadTask.exception } returns Exception()
        every { failureDownloadTask.addOnCompleteListener(capture(downloadSlot)) } answers {
            downloadSlot.captured.onComplete(failureDownloadTask)
            failureDownloadTask
        }

        // Set up successful delete task mock
        every { successDeleteTask.isSuccessful } returns true
        every { successDeleteTask.addOnCompleteListener(capture(deleteSlot)) } answers {
            deleteSlot.captured.onComplete(successDeleteTask)
            successDeleteTask
        }

        // Set up failed delete task mock
        every { failureDeleteTask.isSuccessful } returns false
        every { failureDeleteTask.exception } returns Exception()
        every { failureDeleteTask.addOnCompleteListener(capture(deleteSlot)) } answers {
            deleteSlot.captured.onComplete(failureDeleteTask)
            failureDeleteTask
        }

        // Setup Mock FirebaseStorage
        every { mockStorage.reference } returns mockRef
        every { mockRef.child(imageRefPath) } returns imagesRef

        storageService = StorageServiceImpl(mockStorage)
    }

    @Test
    fun uploadFailure_test() {
        every { imagesRef.putFile(mockUri) } returns failureUploadTask

        storageService.upload(imageRefPath, mockUri, storageCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun uploadSuccess_test() {
        every { imagesRef.putFile(mockUri) } returns successUploadTask

        storageService.upload(imageRefPath, mockUri, storageCallback)
        assert(testState == TestState.SUCCESSFUL)
    }

    @Test
    fun downloadFailure_test() {
        every { imagesRef.getBytes(ONE_MEGABYTE) } returns failureDownloadTask

        storageService.download(imageRefPath, downloadCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun downloadSuccess_test() {
        every { imagesRef.getBytes(ONE_MEGABYTE) } returns successDownloadTask

        storageService.download(imageRefPath, downloadCallback)
        assert(testState == TestState.SUCCESSFUL)
    }

    @Test
    fun deleteFailure_test() {
        every { imagesRef.delete() } returns failureDeleteTask

        storageService.delete(imageRefPath, storageCallback)
        assert(testState == TestState.FAILED)
    }

    @Test
    fun deleteSuccess_test() {
        every { imagesRef.delete() } returns successDeleteTask

        storageService.delete(imageRefPath, storageCallback)
        assert(testState == TestState.SUCCESSFUL)
    }
}