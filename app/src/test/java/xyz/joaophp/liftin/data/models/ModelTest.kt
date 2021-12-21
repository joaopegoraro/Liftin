package xyz.joaophp.liftin.data.models

import org.junit.Test
import xyz.joaophp.liftin.utils.ConversionException
import java.sql.Timestamp

class ModelTest {

    // Constants
    companion object {
        private const val nome = 1
        private const val nomeKey = "nome"

        // Exercise model
        private const val imagemUrl = "imagens/imagem.jpg"
        private const val imagemUrlKey = "imagem"
        private const val observacoes = "Esse exercício é bem legal"
        private const val observacoesKey = "observacoes"
        private const val timestamp = 1639678297320
        private const val timestampKey = "timestamp"

        // Workout model
        private const val descricao = "Esse treino é bem legal"
        private const val descricaoKey = "descricao"

        // User model
        private const val uid = "uid"

    }

    private val user = User(uid)
    private val userHashMap = hashMapOf(uid to uid)

    private val workout = Workout(nome, descricao, timestamp)
    private val workoutHashMap = hashMapOf(
        nomeKey to nome,
        descricaoKey to descricao,
        timestampKey to timestamp
    )

    private val exercise = Exercise(nome, imagemUrl, observacoes)
    private val exerciseHashMap = hashMapOf(
        nomeKey to nome,
        imagemUrlKey to imagemUrl,
        observacoesKey to observacoes
    )


    @Test
    fun toMapFailure_test() {
        val userTest = workout.toMap()
        val workoutTest = exercise.toMap()
        val exerciseTest = user.toMap()

        assert(userTest != userHashMap)
        assert(workoutTest != workoutHashMap)
        assert(exerciseTest != exerciseHashMap)
    }

    @Test
    fun toMapSuccess_test() {
        val userTest = user.toMap()
        val workoutTest = workout.toMap()
        val exerciseTest = exercise.toMap()

        assert(userTest == userHashMap)
        assert(workoutTest == workoutHashMap)
        assert(exerciseTest == exerciseHashMap)
    }

    @Test
    fun fromMapFailure_test() {
        try {
            User.fromMap(workoutHashMap)
            assert(false)
        } catch (e: ConversionException) {
            assert(true)
        }

        try {
            Workout.fromMap(exerciseHashMap)
            assert(false)
        } catch (e: ConversionException) {
            assert(true)
        }

        try {
            Exercise.fromMap(userHashMap)
            assert(false)
        } catch (e: ConversionException) {
            assert(true)
        }
    }

    @Test
    fun fromMapSuccess_test() {
        val userTest = User.fromMap(userHashMap)
        val workoutTest = Workout.fromMap(workoutHashMap)
        val exerciseTest = Exercise.fromMap(exerciseHashMap)

        assert(userTest == user)
        assert(workoutTest == workout)
        assert(exerciseTest == exercise)
    }
}