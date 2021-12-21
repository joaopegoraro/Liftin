package xyz.joaophp.liftin.usecases

import android.net.Uri
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.usecases.images.CreateImageUseCase
import xyz.joaophp.liftin.usecases.images.CreateImageUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ImageFailure

class CreateImageUseCaseTest {

    private lateinit var createImageUseCase: CreateImageUseCase

    // Mock ImageRepository
    private val mockImageRepository = mockk<ImageRepository>()

    // Mock exercise
    private val user = User(uid)

    // Mock Uri
    private val mockUri = mockk<Uri>()

    // Mock error
    private val error = Error<Failure, String>(ImageFailure.EmptyField)

    // Constants
    companion object {
        private const val uid = "uid"
    }

    @Before
    fun setUp() {
        createImageUseCase = CreateImageUseCaseImpl(mockImageRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteImageFailure_test() = runTest {
        coEvery { mockImageRepository.saveImage(user, mockUri) } returns error

        val result = createImageUseCase(user, mockUri)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteImageSuccess_test() = runTest {
        coEvery { mockImageRepository.saveImage(user, mockUri) } returns Success("success!")

        val result = createImageUseCase(user, mockUri)
        assert(result is Success)
    }

}