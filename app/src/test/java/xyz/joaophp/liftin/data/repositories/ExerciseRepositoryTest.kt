package xyz.joaophp.liftin.data.repositories

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
import xyz.joaophp.liftin.data.repositories.exercise.ExerciseRepositoryImpl
import xyz.joaophp.liftin.data.services.database.DatabaseService
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.DatabaseFailure
import java.sql.Timestamp

class ExerciseRepositoryTest {

    private lateinit var exerciseRepository: ExerciseRepository

    // Mock DatabaseService
    private val mockDbService = mockk<DatabaseService>()

    // Mock user
    private val user = User(userUid)

    // Mock workout
    private val workout = Workout(nome, workoutDescricao, workoutTimestamp)

    // Mock exercise
    private val exercise = Exercise(nome, exerciseImg, exerciseDescricao)

    // Workout Path
    private val singlePath =
        "users/$userUid/workouts/${workout.timestamp}-${workout.nome}/exercises/${exercise.nome}"

    // Constants
    companion object {

        private const val nome = 1

        // User
        private const val userUid = "uid"

        // Workout
        private const val workoutDescricao = "esse treino é bem legal"
        private const val workoutTimestamp: Long = 1639848203

        // Exercise
        private const val exerciseImg = "images/image.jpg"
        private const val exerciseDescricao = "esse exercício é bem legal"
    }

    @Before
    fun setUp() {
        exerciseRepository = ExerciseRepositoryImpl(mockDbService)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addExerciseFailure_test() = runTest {
        coEvery { mockDbService.set(exercise, singlePath) } returns Error(DatabaseFailure.NotFound)

        val result = exerciseRepository.addExercise(user, workout, exercise)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addExerciseSuccess_test() = runTest {
        coEvery { mockDbService.set(exercise, singlePath) } returns Success(exercise)

        val result = exerciseRepository.addExercise(user, workout, exercise)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteExerciseFailure_test() = runTest {
        coEvery {
            mockDbService.delete(exercise, singlePath)
        } returns Error(DatabaseFailure.NotFound)

        val result = exerciseRepository.deleteExercise(user, workout, exercise)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteExerciseSuccess_test() = runTest {
        coEvery { mockDbService.delete(exercise, singlePath) } returns Success(exercise)

        val result = exerciseRepository.deleteExercise(user, workout, exercise)
        assert(result is Success)
    }
}