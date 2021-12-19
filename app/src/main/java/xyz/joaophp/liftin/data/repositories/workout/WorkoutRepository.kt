package xyz.joaophp.liftin.data.repositories.workout

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface WorkoutRepository {

    @ExperimentalCoroutinesApi
    suspend fun getWorkouts(user: User): Flow<Either<Failure, List<Workout?>?>>

    suspend fun addWorkout(workout: Workout, user: User): Either<Failure, Workout>
    suspend fun deleteWorkout(workout: Workout, user: User): Either<Failure, Workout>
}