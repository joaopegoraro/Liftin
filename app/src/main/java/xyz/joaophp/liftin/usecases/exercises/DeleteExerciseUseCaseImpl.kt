package xyz.joaophp.liftin.usecases.exercises

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.exercise.ExerciseRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

class DeleteExerciseUseCaseImpl(
    private val exerciseRepository: ExerciseRepository
) : DeleteExerciseUseCase {

    override suspend fun invoke(
        user: User,
        workout: Workout,
        exercise: Exercise
    ): Either<Failure, Exercise> {
        return exerciseRepository.deleteExercise(user, workout, exercise)
    }
}