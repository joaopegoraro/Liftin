package xyz.joaophp.liftin.usecases.workouts

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

class DeleteWorkoutUseCaseImpl(
    private val workoutRepository: WorkoutRepository
) : DeleteWorkoutUseCase {

    override suspend fun invoke(workout: Workout, user: User): Either<Failure, Workout> {
        return workoutRepository.deleteWorkout(workout, user)
    }
}