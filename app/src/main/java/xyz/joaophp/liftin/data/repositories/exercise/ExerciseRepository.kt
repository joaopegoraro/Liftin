package xyz.joaophp.liftin.data.repositories.exercise

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface ExerciseRepository {

    @ExperimentalCoroutinesApi
    suspend fun getExercises(
        user: User,
        workout: Workout
    ): Flow<Either<Failure, List<Exercise?>?>>

    suspend fun addExercise(
        user: User,
        workout: Workout,
        exercise: Exercise
    ): Either<Failure, Exercise>

    suspend fun deleteExercise(
        user: User,
        workout: Workout,
        exercise: Exercise
    ): Either<Failure, Exercise>
}