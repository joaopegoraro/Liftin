package xyz.joaophp.liftin.data.repositories.exercise

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.services.database.DatabaseService
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.Error
import xyz.joaophp.liftin.utils.Success
import xyz.joaophp.liftin.utils.failures.ExerciseFailure
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ModelFailure

class ExerciseRepositoryImpl(
    private val databaseService: DatabaseService
) : ExerciseRepository {

    private val exercisesPath = fun(user: User, workout: Workout) =
        "users/${user.uid}/workouts/${workout.timestamp}-${workout.nome}/exercises"

    private val exercisePath = fun(user: User, workout: Workout, exercise: Exercise) =
        "${exercisesPath(user, workout)}/${exercise.nome}"

    @ExperimentalCoroutinesApi
    override suspend fun getExercises(
        user: User,
        workout: Workout
    ): Flow<Either<Failure, List<Exercise?>?>> {
        val path = exercisesPath(user, workout)
        return databaseService.getAll(path).map { either ->
            try {
                either.fold(
                    ifError = { Error(it) },
                    ifSuccess = { list ->
                        val exercises = list?.map { hashMap ->
                            hashMap?.let { Exercise.fromMap(it) as Exercise }
                        }
                        Success(exercises)
                    }
                )
            } catch (e: Exception) {
                Error(ModelFailure.FailedConversion(e))
            }
        }
    }

    override suspend fun addExercise(
        user: User,
        workout: Workout,
        exercise: Exercise
    ): Either<Failure, Exercise> {
        val path = exercisePath(user, workout, exercise)
        return databaseService.set(exercise, path).fold(
            ifError = { Error(it) },
            ifSuccess = {
                if (it is Exercise) {
                    Success(it)
                } else {
                    Error(ExerciseFailure.WrongModel)
                }
            }
        )
    }

    override suspend fun deleteExercise(
        user: User,
        workout: Workout,
        exercise: Exercise
    ): Either<Failure, Exercise> {
        val path = exercisePath(user, workout, exercise)
        return databaseService.delete(exercise, path).fold(
            ifError = { Error(it) },
            ifSuccess = {
                if (it is Exercise) {
                    Success(it)
                } else {
                    Error(ExerciseFailure.WrongModel)
                }
            }
        )
    }
}