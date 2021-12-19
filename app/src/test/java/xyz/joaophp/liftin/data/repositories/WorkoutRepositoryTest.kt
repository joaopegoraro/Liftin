package xyz.joaophp.liftin.data.repositories;

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepository
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepositoryImpl
import xyz.joaophp.liftin.data.services.database.DatabaseService
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.DatabaseFailure
import java.sql.Timestamp

class WorkoutRepositoryTest {

    private lateinit var workoutRepository: WorkoutRepository

    // Mock DatabaseService
    private val mockDbService = mockk<DatabaseService>()

    // Mock user
    private val user = User(userUid)

    // Mock workout
    private val workout = Workout(workoutName, workoutDescricao, Timestamp(workoutTimestamp))
    private val workouts = listOf(workout, workout, workout)

    // Workout Path
    private val singlePath = "users/$userUid/workouts/${workout.timestamp}-${workout.nome}"

    // Constants
    companion object {

        // User
        private const val userUid = "uid"

        // Workout
        private const val workoutName = 1
        private const val workoutDescricao = "esse treino é bem legal"
        private const val workoutTimestamp: Long = 1639848203

        // Paths
        private const val path = "users/$userUid/workouts"
    }

    @Before
    fun setUp() {
        workoutRepository = WorkoutRepositoryImpl(mockDbService)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addWorkoutFailure_test() = runTest {
        coEvery { mockDbService.set(workout, singlePath) } returns Error(DatabaseFailure.NotFound)

        val result = workoutRepository.addWorkout(workout, user)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addWorkoutSuccess_test() = runTest {
        coEvery { mockDbService.set(workout, singlePath) } returns Success(workout)

        val result = workoutRepository.addWorkout(workout, user)
        assert(result is Success)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteWorkoutFailure_test() = runTest {
        coEvery {
            mockDbService.delete(workout, singlePath)
        } returns Error(DatabaseFailure.NotFound)

        val result = workoutRepository.deleteWorkout(workout, user)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteWorkoutSuccess_test() = runTest {
        coEvery { mockDbService.delete(workout, singlePath) } returns Success(workout)

        val result = workoutRepository.deleteWorkout(workout, user)
        assert(result is Success)
    }
}
