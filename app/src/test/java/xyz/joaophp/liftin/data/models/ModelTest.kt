package xyz.joaophp.liftin.data.models

import org.junit.Test
import xyz.joaophp.liftin.utils.Error
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

    private val workout = Workout(nome, descricao, Timestamp(timestamp))
    private val workoutHashMap = hashMapOf(
        nomeKey to nome,
        descricaoKey to descricao,
        timestampKey to Timestamp(timestamp)
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
        val userTest = user.fromMap(workoutHashMap)
        val workoutTest = workout.fromMap(exerciseHashMap)
        val exerciseTest = exercise.fromMap(userHashMap)

        assert(userTest is Error)
        assert(workoutTest is Error)
        assert(exerciseTest is Error)
    }

    @Test
    fun fromMapSuccess_test() {
        val userTest = user.fromMap(userHashMap)
        val workoutTest = workout.fromMap(workoutHashMap)
        val exerciseTest = exercise.fromMap(exerciseHashMap)

        userTest.fold(
            ifError = { assert(false) },
            ifSuccess = { assert(it == user) }
        )
        workoutTest.fold(
            ifError = { assert(false) },
            ifSuccess = { assert(it == workout) }
        )
        exerciseTest.fold(
            ifError = { assert(false) },
            ifSuccess = { assert(it == exercise) }
        )
    }
}