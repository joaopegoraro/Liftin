package xyz.joaophp.liftin.usecases.workouts

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

class GetWorkoutsUseCaseImpl(
    private val workoutRepository: WorkoutRepository
) : GetWorkoutsUseCase {

    @ExperimentalCoroutinesApi
    override suspend fun invoke(user: User): Flow<Either<Failure, List<Workout?>?>> {
        return workoutRepository.getWorkouts(user)
    }
}