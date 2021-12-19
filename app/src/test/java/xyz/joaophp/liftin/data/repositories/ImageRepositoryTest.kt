package xyz.joaophp.liftin.data.repositories

import android.net.Uri
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.data.repositories.image.ImageRepositoryImpl
import xyz.joaophp.liftin.data.services.storage.StorageService
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.StorageFailure

class ImageRepositoryTest {

    private lateinit var imageRepository: ImageRepository

    // Mock StorageService
    private val mockStorageService = mockk<StorageService>()

    // Mock objects
    private val mockUri = mockk<Uri>()

    // Mock user
    private val user = User(uid)

    // Mock errors
    private val uploadError = Error<Failure, Uri?>(StorageFailure.Timeout)
    private val downloadError = Error<Failure, ByteArray>(StorageFailure.Timeout)
    private val deleteError = Error<Failure, Unit>(StorageFailure.NotFound)

    // Constants
    companion object {
        private const val uid = "uid"
        private const val uploadPath = "image/123/0x00/image.jpg"
    }

    @Before
    fun setUp() {
        mockkStatic(Uri::class)
        imageRepository = ImageRepositoryImpl(mockStorageService)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun saveImageExceptionFailure_test() = runTest {
        coEvery { mockStorageService.upload(uploadPath, mockUri) } throws Exception()

        val result = imageRepository.saveImage(user, mockUri)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun saveImageFailure_test() = runTest {
        coEvery { mockStorageService.upload(uploadPath, mockUri) } returns uploadError

        val result = imageRepository.saveImage(user, mockUri)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun saveImageSuccess_test() = runTest {
        coEvery { mockStorageService.upload(any(), mockUri) } returns Success(mockUri)

        val result = imageRepository.saveImage(user, mockUri)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getImageFailure_test() = runTest {
        coEvery { mockStorageService.download(uploadPath) } returns downloadError

        val result = imageRepository.getImage(uploadPath)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getImageSuccess_test() = runTest {
        coEvery { mockStorageService.download(uploadPath) } returns Success(byteArrayOf())

        val result = imageRepository.getImage(uploadPath)
        assert(result is Success)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun deleteImageFailure_test() = runTest {
        coEvery { mockStorageService.delete(uploadPath) } returns deleteError

        val result = imageRepository.deleteImage(uploadPath)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteImageSuccess_test() = runTest {
        coEvery { mockStorageService.delete(uploadPath) } returns Success(Unit)

        val result = imageRepository.deleteImage(uploadPath)
        assert(result is Success)
    }

}