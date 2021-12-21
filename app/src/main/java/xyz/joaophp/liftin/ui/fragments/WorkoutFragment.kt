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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import xyz.joaophp.liftin.R
import xyz.joaophp.liftin.data.models.Exercise
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.databinding.FragmentWorkoutBinding
import xyz.joaophp.liftin.ui.adapters.ExerciseAdapter
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.ui.viewmodels.WorkoutViewModel
import xyz.joaophp.liftin.utils.failures.DatabaseFailure
import xyz.joaophp.liftin.utils.failures.ExerciseFailure
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.StorageFailure

@AndroidEntryPoint
class WorkoutFragment : Fragment() {

    private var binding: FragmentWorkoutBinding? = null

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: WorkoutViewModel by viewModels()

    private lateinit var user: User
    private lateinit var workout: Workout

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
        binding = FragmentWorkoutBinding.inflate(inflater, container, false)

        // Set up workout list adapter
        val exerciseAdapter = ExerciseAdapter()
        exerciseAdapter.addOnClickListener { exercise ->
            navigateToExerciseFragment(exercise)
        }
        exerciseAdapter.addOnDeleteClickListener { workout ->
            deleteExercise(workout)
        }

        // Set up workout list
        binding?.rvExerciseList?.adapter = exerciseAdapter

        // Fab click listener
        binding?.apply {
            fab.setOnClickListener { navigateToAddExerciseFragment() }
        }

        // Flow collectors
        setUpCollectors()

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // Action methods

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
                ifSuccess = { /* Everything went smoothly! */ }
            )
        }
    }

    // Handle flows

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setUpCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.getExercises(user, workout).collect { either ->
                either.fold(
                    ifError = { failure -> handleFailure(failure) },
                    ifSuccess = { exerciseList ->
                        val adapter = binding?.rvExerciseList?.adapter as ExerciseAdapter?
                        adapter?.submitList(exerciseList)
                    }
                )
            }
        }
    }

    // Handle AppState

    private fun handleState(state: AppState?) {
        when (state) {
            is AppState.InWorkout -> {
                user = state.user
                workout = state.workout
            }
            else -> Unit
        }
    }

    // Navigation methods

    private fun navigateToExerciseFragment(exercise: Exercise) {
        appViewModel.updateState(AppState.InExercise(user, workout, exercise))
        findNavController().navigate(R.id.action_workoutFragment_to_exerciseFragment)

    }

    private fun navigateToAddExerciseFragment() {
        appViewModel.updateState(AppState.InAddExercise(user, workout))
        findNavController().navigate(R.id.action_workoutFragment_to_addExerciseFragment)
    }

    // Handle errors

    private fun handleFailure(failure: Failure) {
        when (failure) {
            is ExerciseFailure.WrongModel -> getString(R.string.wrong_model)
            is StorageFailure.Unauthorised -> getString(R.string.unauthorised)
            is StorageFailure.NotFound -> getString(R.string.image_deletion_fail)
            is StorageFailure.Timeout -> getString(R.string.timeout)
            is StorageFailure.LimitExceeded -> getString(R.string.download_limit_exceeded)
            is DatabaseFailure.NotFound -> getString(R.string.exercises_not_found)
            is DatabaseFailure.Timeout -> getString(R.string.timeout)
            is DatabaseFailure.InvalidQuery -> getString(R.string.invalid_query)
            is DatabaseFailure.Unavailable -> getString(R.string.unavailable)
            is DatabaseFailure.DataLost -> getString(R.string.data_lost)
            is DatabaseFailure.Unauthorised -> getString(R.string.unauthorised)
            else -> displayMessage("An unexpected error has occurred\n" + failure.e)
        }
    }

    private fun displayMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }
}