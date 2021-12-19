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
import xyz.joaophp.liftin.usecases.exercises.CreateExerciseUseCase
import xyz.joaophp.liftin.usecases.exercises.CreateExerciseUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.ExerciseFailure
import java.sql.Timestamp

class CreateExerciseUseCaseTest {

    private lateinit var createExerciseUseCase: CreateExerciseUseCase

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
        createExerciseUseCase = CreateExerciseUseCaseImpl(mockExerciseRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun createFailure_test() = runTest {
        coEvery {
            mockExerciseRepository.addExercise(user, workout, exercise)
        } returns Error(ExerciseFailure.WrongModel)

        val result = createExerciseUseCase(user, workout, exercise)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun createSuccess_test() = runTest {
        coEvery {
            mockExerciseRepository.addExercise(user, workout, exercise)
        } returns Success(exercise)

        val result = createExerciseUseCase(user, workout, exercise)
        assert(result is Success)

    }
}