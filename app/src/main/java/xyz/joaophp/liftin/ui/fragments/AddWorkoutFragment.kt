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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddWorkoutBinding.inflate(inflater, container, false)

        // Handle state
        lifecycleScope.launchWhenStarted {
            appViewModel.appState.collect { state -> handleState(state) }
        }

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
        lifecycleScope.launchWhenCreated {
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
        when (failure) {
            is WorkoutFailure.WrongModel ->
                displayError("There was a big problem creating your workout.\nContact the developer.")
            is WorkoutFailure.CreationFailure ->
                displayError("There was a problem creating your workout\n${failure.e}")
            is DatabaseFailure.Timeout ->
                displayError("Can't reach our servers.\nCheck your connection or contact the developer.")
            is DatabaseFailure.DocumentAlreadyExists ->
                displayError("A workout with this name and date already exists")
            is DatabaseFailure.InvalidQuery ->
                displayError("There was a huge problem creating your workout.\nContact the developer.")
            is DatabaseFailure.Unavailable ->
                displayError("Our servers are currently unavailable.\n Try again later.")
            is DatabaseFailure.DataLost ->
                displayError("There was a problem with your connection.\nTry again.")
            is DatabaseFailure.Unauthorised ->
                displayError("There appears to be a problem with your authentication.\n Try to login in again")
            else -> {
                failure.e?.printStackTrace()
                displayError("An unknown error has happened:\n${failure.e}")
            }
        }
    }

    private fun displayError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }
}