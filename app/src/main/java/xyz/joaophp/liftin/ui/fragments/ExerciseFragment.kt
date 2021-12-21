package xyz.joaophp.liftin.ui.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import xyz.joaophp.liftin.R
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.databinding.FragmentExerciseBinding
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.ui.viewmodels.ExerciseViewModel
import xyz.joaophp.liftin.utils.failures.Failure


@AndroidEntryPoint
class ExerciseFragment : Fragment() {

    private var binding: FragmentExerciseBinding? = null

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: ExerciseViewModel by viewModels()

    private lateinit var user: User
    private lateinit var workout: Workout
    private lateinit var exercise: Exercise

    // Lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle state
        lifecycleScope.launchWhenCreated {
            appViewModel.appState.collect { state -> handleState(state) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExerciseBinding.inflate(inflater, container, false)

        // Bind exercise information to screen elements
        binding?.apply {
            tvNomeValue.text = exercise.nome.toString()
            tvObservacaoValue.text = exercise.observacoes
            setImage()
        }

        // Enable delete button in the action bar
        setHasOptionsMenu(true)

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // Options menu methods

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_exercise, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_icon -> {
                deleteExercise(exercise)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Action methods

    private fun setImage() {
        lifecycleScope.launchWhenStarted {
            viewModel.getImage(exercise).fold(
                ifError = { failure -> handleFailure(failure) },
                ifSuccess = { byteArray ->
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    binding?.ivImage?.setImageBitmap(bitmap)
                }
            )
        }
    }

    private fun deleteExercise(exercise: Exercise) {
        lifecycleScope.launchWhenStarted {
            viewModel.deleteExercise(user, workout, exercise).fold(
                ifError = { failure -> handleFailure(failure) },
                ifSuccess = { deletedExercise ->
                    displayMessage("The exercise ${deletedExercise.nome} has been deleted")
                    deleteImage(exercise)
                }
            )
        }
    }

    private fun deleteImage(exercise: Exercise) {
        lifecycleScope.launchWhenStarted {
            viewModel.deleteImage(exercise).fold(
                ifError = { failure -> handleFailure(failure) },
                ifSuccess = { navigateToWorkoutFragment() }
            )
        }
    }


    // Handle AppState

    private fun handleState(state: AppState?) {
        when (state) {
            is AppState.InExercise -> {
                user = state.user
                workout = state.workout
                exercise = state.exercise
            }
            else -> Unit
        }
    }

    // Navigation methods

    private fun navigateToWorkoutFragment() {
        appViewModel.updateState(AppState.InWorkout(user, workout))
        findNavController().navigateUp()

    }

    // Handle errors

    private fun handleFailure(failure: Failure) {
        when (failure) {
            else -> displayMessage("An unexpected error has occurred\n" + failure.e)
        }
    }

    private fun displayMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }
}