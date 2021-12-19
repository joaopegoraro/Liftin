package xyz.joaophp.liftin.usecases.workouts

import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface DeleteWorkoutUseCase {

    suspend operator fun invoke(workout: Workout, user: User): Either<Failure, Workout>
}