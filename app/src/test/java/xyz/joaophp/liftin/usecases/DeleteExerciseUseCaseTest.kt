package xyz.joaophp.liftin.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.exercise.ExerciseRepository
import xyz.joaophp.liftin.usecases.exercises.DeleteExerciseUseCase
import xyz.joaophp.liftin.usecases.exercises.DeleteExerciseUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.ExerciseFailure
import java.sql.Timestamp

class DeleteExerciseUseCaseTest {

    private lateinit var deleteExerciseUseCase: DeleteExerciseUseCase

    // Mock WorkoutRepository
    private val mockExerciseRepository = mockk<ExerciseRepository>()

    // Mock models
    private val exercise = Exercise(nome, imageUrl, observacoes)
    private val workout = Workout(nome, descricao, Timestamp(timestamp))
    private val user = User(uid)

    // Constants
    companion object {
        private const val nome = 1

        // User
        private const val uid = "uid"

        // Workout
        private const val descricao = "esse treino é bem legal"
        private const val timestamp: Long = 1639928341

        // Exercise
        private const val observacoes = "esse exercício é bem legal"
        private const val imageUrl = "imagens/imagem.jpg"
    }

    @Before
    fun setUp() {
        deleteExerciseUseCase = DeleteExerciseUseCaseImpl(mockExerciseRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteFailure_test() = runTest {
        coEvery {
            mockExerciseRepository.deleteExercise(user, workout, exercise)
        } returns Error(ExerciseFailure.WrongModel)

        val result = deleteExerciseUseCase(user, workout, exercise)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteSuccess_test() = runTest {
        coEvery {
            mockExerciseRepository.deleteExercise(user, workout, exercise)
        } returns Success(exercise)

        val result = deleteExerciseUseCase(user, workout, exercise)
        assert(result is Success)

    }
}