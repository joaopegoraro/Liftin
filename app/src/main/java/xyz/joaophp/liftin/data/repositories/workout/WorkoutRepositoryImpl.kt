package xyz.joaophp.liftin.data.repositories.workout

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.services.database.DatabaseService
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ModelFailure
import xyz.joaophp.liftin.utils.failures.WorkoutFailure

class WorkoutRepositoryImpl(
    private val databaseService: DatabaseService
) : WorkoutRepository {

    private val workoutsPath = fun(user: User) = "users/${user.uid}/workouts"
    private val workoutPath = fun(user: User, workout: Workout) =
        "${workoutsPath(user)}/${workout.timestamp}-${workout.nome}"

    @ExperimentalCoroutinesApi
    override suspend fun getWorkouts(user: User): Flow<Either<Failure, List<Workout?>?>> {
        val path = workoutsPath(user)
        return databaseService.getAll(path).map { either ->
            try {
                either.fold(
                    ifError = { Error(it) },
                    ifSuccess = { list ->
                        val workouts = list?.map { hashMap ->
                            hashMap?.let { Workout.fromMap(it) as Workout }
                        }
                        Success(workouts)
                    }
                )
            } catch (e: Exception) {
                Error(ModelFailure.FailedConversion(e))
            }
        }
    }

    override suspend fun addWorkout(workout: Workout, user: User): Either<Failure, Workout> {
        val path = workoutPath(user, workout)
        return databaseService.set(workout, path).fold(
            ifError = { Error(it) },
            ifSuccess = {
                if (it is Workout) {
                    Success(it)
                } else {
                    Error(WorkoutFailure.WrongModel)
                }
            }
        )
    }

    override suspend fun deleteWorkout(workout: Workout, user: User): Either<Failure, Workout> {
        val path = workoutPath(user, workout)
        return databaseService.delete(workout, path).fold(
            ifError = { Error(it) },
            ifSuccess = {
                if (it is Workout) {
                    Success(it)
                } else {
                    Error(WorkoutFailure.WrongModel)
                }
            }
        )
    }
}