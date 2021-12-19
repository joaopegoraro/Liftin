package xyz.joaophp.liftin.usecases.workouts

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.workout.WorkoutRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

class CreateWorkoutUseCaseImpl(
    private val workoutRepository: WorkoutRepository
) : CreateWorkoutUseCase {

    override suspend fun invoke(workout: Workout, user: User): Either<Failure, Workout> {
        return workoutRepository.addWorkout(workout, user)
    }
}