package xyz.joaophp.liftin.usecases.exercises

import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.exercise.ExerciseRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

class CreateExerciseUseCaseImpl(
    private val exerciseRepository: ExerciseRepository
) : CreateExerciseUseCase {

    override suspend fun invoke(
        user: User,
        workout: Workout,
        exercise: Exercise
    ): Either<Failure, Exercise> {
        return exerciseRepository.addExercise(user, workout, exercise)
    }
}