package xyz.joaophp.liftin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import xyz.joaophp.liftin.R
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.databinding.FragmentAddWorkoutBinding
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AddWorkoutViewModel
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.utils.failures.DatabaseFailure
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.WorkoutFailure

@AndroidEntryPoint
class AddWorkoutFragment : Fragment() {

    private var binding: FragmentAddWorkoutBinding? = null

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: AddWorkoutViewModel by viewModels()

    private lateinit var user: User

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
        binding = FragmentAddWorkoutBinding.inflate(inflater, container, false)

        // Fab click listener
        binding?.fab?.setOnClickListener { createWorkout() }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // Action methods

    private fun createWorkout() {

        // Show loading animation
        binding?.loading?.show()
        binding?.root?.alpha = 0.5f // lowers opacity during loading

        // Get input fields values
        val nome = binding?.tfNome?.text?.toString()
        val descricao = binding?.tfDescricao?.text?.toString()

        // Check if they are null or empty
        if (nome.isNullOrEmpty() || descricao.isNullOrEmpty()) {

            // Hide loading animation
            binding?.root?.alpha = 1f // opacity goes back to normal
            binding?.loading?.hide()

            return handleFailure(WorkoutFailure.EmptyFields)
        }

        // Create workout
        lifecycleScope.launchWhenStarted {
            viewModel.createWorkout(nome.toInt(), descricao, user).fold(
                ifError = { failure -> handleFailure(failure) },
                ifSuccess = { navigateToHomeFragment() }
            )
        }

        // Hide loading animation
        binding?.root?.alpha = 1f // opacity goes back to normal
        binding?.loading?.hide()
    }

    // Handle AppState

    private fun handleState(state: AppState?) {
        when (state) {
            is AppState.InAddWorkout -> user = state.user
            else -> Unit
        }
    }

    // Navigation methods

    private fun navigateToHomeFragment() {
        appViewModel.updateState(AppState.InHome(user))
        findNavController().navigateUp()
    }

    // Handle errors

    private fun handleFailure(failure: Failure) {
        failure.e?.printStackTrace()
        val message = when (failure) {
            is WorkoutFailure.EmptyFields -> getString(R.string.workout_empty_fields)
            is WorkoutFailure.WrongModel -> getString(R.string.wrong_model)
            is WorkoutFailure.CreationFailure -> getString(R.string.workout_creation_failure)
            is DatabaseFailure.Timeout -> getString(R.string.timeout)
            is DatabaseFailure.DocumentAlreadyExists -> getString(R.string.workout_already_exists)
            is DatabaseFailure.InvalidQuery -> getString(R.string.invalid_query)
            is DatabaseFailure.Unavailable -> getString(R.string.unavailable)
            is DatabaseFailure.DataLost -> getString(R.string.data_lost)
            is DatabaseFailure.Unauthorised -> getString(R.string.unauthorised)
            else -> getString(R.string.unknown_error)
        }
        displayError(message)
    }

    private fun displayError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }
}