package xyz.joaophp.liftin.usecases.workouts

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface GetWorkoutsUseCase {

    @ExperimentalCoroutinesApi
    suspend operator fun invoke(user: User): Flow<Either<Failure, List<Workout?>?>>
}