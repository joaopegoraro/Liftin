package xyz.joaophp.liftin.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.usecases.exercises.DeleteExerciseUseCase
import xyz.joaophp.liftin.usecases.images.DeleteImageUseCase
import xyz.joaophp.liftin.usecases.images.GetImageUseCase
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val getImageUseCase: GetImageUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase
) : ViewModel() {

    suspend fun getImage(exercise: Exercise): Either<Failure, ByteArray> {
        return getImageUseCase(exercise)
    }

    suspend fun deleteExercise(user: User, workout: Workout, exercise: Exercise) =
        deleteExerciseUseCase(user, workout, exercise)

    suspend fun deleteImage(exercise: Exercise): Either<Failure, Unit> {
        return deleteImageUseCase(exercise)
    }
}