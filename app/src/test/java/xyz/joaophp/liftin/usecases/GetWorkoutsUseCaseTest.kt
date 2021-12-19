package xyz.joaophp.liftin.usecases

import io.mockk.mockk
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepository
import xyz.joaophp.liftin.usecases.workouts.GetWorkoutsUseCase

class GetWorkoutsUseCaseTest {

    private lateinit var getWorkoutsUseCase: GetWorkoutsUseCase

    // Mock WorkoutRepository
    private val mockWorkoutRepository = mockk<WorkoutRepository>()

    // Mock User
    private val user = User(uid)

    // Constants
    companion object {
        private const val uid = "uid"
    }

}