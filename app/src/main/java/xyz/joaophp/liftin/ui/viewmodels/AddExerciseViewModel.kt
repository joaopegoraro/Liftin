package xyz.joaophp.liftin.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.usecases.exercises.CreateExerciseUseCase
import xyz.joaophp.liftin.usecases.images.CreateImageUseCase
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val createExerciseUseCase: CreateExerciseUseCase,
    private val createImageUseCase: CreateImageUseCase
) : ViewModel() {

    suspend fun createExercise(
        user: User,
        workout: Workout,
        name: Number,
        observations: String,
        imageUrl: String
    ): Either<Failure, Exercise> {
        val exercise = Exercise(name, imageUrl, observations)
        return createExerciseUseCase(user, workout, exercise)
    }

    suspend fun createImage(user: User, contentUri: Uri): Either<Failure, Uri> {
        return createImageUseCase(user, contentUri)
    }
}