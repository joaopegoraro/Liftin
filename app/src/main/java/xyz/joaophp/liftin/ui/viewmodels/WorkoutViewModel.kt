package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.usecases.exercises.DeleteExerciseUseCase
import xyz.joaophp.liftin.usecases.exercises.GetExercisesUseCase
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase
) : ViewModel() {

    @ExperimentalCoroutinesApi
    suspend fun getExercises(user: User, workout: Workout) = getExercisesUseCase(user, workout)

    suspend fun deleteExercise(user: User, workout: Workout, exercise: Exercise) =
        deleteExerciseUseCase(user, workout, exercise)

}