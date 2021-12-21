package xyz.joaophp.liftin.usecases

import io.mockk.mockk
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.data.repositories.exercise.ExerciseRepository
import xyz.joaophp.liftin.usecases.exercises.GetExercisesUseCase
import java.sql.Timestamp

class GetExercisesUseCaseTest {
    private lateinit var getExercisesUseCase: GetExercisesUseCase

    // Mock WorkoutRepository
    private val mockExerciseRepository = mockk<ExerciseRepository>()

    // Mock models
    private val workout = Workout(nome, descricao, timestamp)
    private val user = User(uid)

    // Constants
    companion object {
        private const val nome = 1

        // User
        private const val uid = "uid"

        // Workout
        private const val descricao = "esse treino é bem legal"
        private const val timestamp: Long = 1639928341
    }
}