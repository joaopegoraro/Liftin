package xyz.joaophp.liftin.usecases.exercises

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.exercise.ExerciseRepository
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

class GetExercisesUseCaseImpl(
    private val exerciseRepository: ExerciseRepository
) : GetExercisesUseCase {

    @ExperimentalCoroutinesApi
    override suspend fun invoke(
        user: User,
        workout: Workout
    ): Flow<Either<Failure, List<Exercise?>?>> {
        return exerciseRepository.getExercises(user, workout)
    }
}