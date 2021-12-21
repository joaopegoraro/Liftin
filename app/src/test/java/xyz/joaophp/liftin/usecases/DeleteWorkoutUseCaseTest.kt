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
import xyz.joaophp.liftin.usecases.workouts.DeleteWorkoutUseCase
import xyz.joaophp.liftin.usecases.workouts.DeleteWorkoutUseCaseImpl
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.WorkoutFailure
import java.sql.Timestamp

class DeleteWorkoutUseCaseTest {

    private lateinit var deleteWorkoutUseCase: DeleteWorkoutUseCase

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
        deleteWorkoutUseCase = DeleteWorkoutUseCaseImpl(mockWorkoutRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteFailure_test() = runTest {
        coEvery {
            mockWorkoutRepository.deleteWorkout(workout, user)
        } returns Error(WorkoutFailure.WrongModel)

        val result = deleteWorkoutUseCase(workout, user)
        assert(result is Error)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteSuccess_test() = runTest {
        coEvery { mockWorkoutRepository.deleteWorkout(workout, user) } returns Success(workout)

        val result = deleteWorkoutUseCase(workout, user)
        assert(result is Success)
    }
}