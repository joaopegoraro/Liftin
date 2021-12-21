package xyz.joaophp.liftin.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.repositories.image.ImageRepository
import xyz.joaophp.liftin.usecases.images.DeleteImageUseCase
import xyz.joaophp.liftin.usecases.images.DeleteImageUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ImageFailure

class DeleteImageUseCaseTest {

    private lateinit var deleteImageUseCase: DeleteImageUseCase

    // Mock ImageRepository
    private val mockImageRepository = mockk<ImageRepository>()

    // Mock exercise
    private val exercise = Exercise(nome, imageUrl, observacoes)

    // Mock error
    private val error = Error<Failure, Unit>(ImageFailure.EmptyField)

    // Constants
    companion object {
        private const val nome = 1
        private const val imageUrl = "imagem/imagem.jpg"
        private const val observacoes = "esse exercício é bem legal"
    }

    @Before
    fun setUp() {
        deleteImageUseCase = DeleteImageUseCaseImpl(mockImageRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteImageFailure_test() = runTest {
        coEvery { mockImageRepository.deleteImage(imageUrl) } returns error

        val result = deleteImageUseCase(exercise)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteImageSuccess_test() = runTest {
        coEvery { mockImageRepository.deleteImage(imageUrl) } returns Success(Unit)

        val result = deleteImageUseCase(exercise)
        assert(result is Success)
    }

}