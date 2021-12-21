package xyz.joaophp.liftin.usecases

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepository
import xyz.joaophp.liftin.usecases.workouts.CreateWorkoutUseCase
import xyz.joaophp.liftin.usecases.workouts.CreateWorkoutUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.WorkoutFailure
import java.sql.Timestamp

class CreateWorkoutUseCaseTest {

    private lateinit var createWorkoutUseCase: CreateWorkoutUseCase

    // Mock WorkoutRepository
    private val mockWorkoutRepository = mockk<WorkoutRepository>()

    // Mock models
    private val workout = Workout(nome, descricao, timestamp)
    private val user = User(uid)

    // Constants
    companion object {
        private const val uid = "uid"
        private const val nome = 1
        private const val descricao = "esse treino é bem legal"
        private const val timestamp: Long = 1639928341
    }

    @Before
    fun setUp() {
        createWorkoutUseCase = CreateWorkoutUseCaseImpl(mockWorkoutRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun createFailure_test() = runTest {
        coEvery {
            mockWorkoutRepository.addWorkout(workout, user)
        } returns Error(WorkoutFailure.WrongModel)

        val result = createWorkoutUseCase(workout, user)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun createSuccess_test() = runTest {
        coEvery {
            mockWorkoutRepository.addWorkout(workout, user)
        } returns Success(workout)

        val result = createWorkoutUseCase(workout, user)
        assert(result is Success)

    }
}