package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.usecases.auth.GetUserUseCase
import xyz.joaophp.liftin.usecases.auth.SignOutUseCase
import xyz.joaophp.liftin.usecases.workouts.DeleteWorkoutUseCase
import xyz.joaophp.liftin.usecases.workouts.GetWorkoutsUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val deleteWorkoutUseCase: DeleteWorkoutUseCase
) : ViewModel() {

    fun getUser() = getUserUseCase()
    fun signOut() = signOutUseCase()

    @ExperimentalCoroutinesApi
    suspend fun getWorkouts(user: User) = getWorkoutsUseCase(user)
    suspend fun deleteWorkout(user: User, workout: Workout) = deleteWorkoutUseCase(workout, user)
}