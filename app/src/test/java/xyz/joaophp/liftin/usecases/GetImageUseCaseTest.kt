package xyz.joaophp.liftin.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.usecases.images.GetImageUseCase
import xyz.joaophp.liftin.usecases.images.GetImageUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ImageFailure

class GetImageUseCaseTest {

    private lateinit var getImageUseCase: GetImageUseCase

    // Mock ImageRepository
    private val mockImageRepository = mockk<ImageRepository>()

    // Mock Exercise
    private val user = User(uid)

    // Mock exercise
    private val exercise = Exercise(nome, imageUrl, observacoes)

    // Mock error
    private val error = Error<Failure, ByteArray>(ImageFailure.FailedUpload(Exception()))

    // Constants
    companion object {
        private const val nome = 1
        private const val imageUrl = "imagem/imagem.jpg"
        private const val observacoes = "esse exercício é bem legal"
        private const val uid = "uid"
    }

    @Before
    fun setUp() {
        getImageUseCase = GetImageUseCaseImpl(mockImageRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getImageFailure_test() = runTest {
        coEvery { mockImageRepository.getImage(imageUrl) } returns error

        val result = getImageUseCase(exercise)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getImageSuccess_test() = runTest {
        coEvery { mockImageRepository.getImage(imageUrl) } returns Success(byteArrayOf())

        val result = getImageUseCase(exercise)
        assert(result is Success)
    }
}