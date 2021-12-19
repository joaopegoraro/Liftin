package xyz.joaophp.liftin.usecases.exercises

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface CreateExerciseUseCase {

    suspend operator fun invoke(
        user: User,
        workout: Workout,
        exercise: Exercise
    ): Either<Failure, Exercise>
}