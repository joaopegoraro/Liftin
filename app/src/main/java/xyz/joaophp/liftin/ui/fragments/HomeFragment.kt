package xyz.joaophp.liftin.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import xyz.joaophp.liftin.MainActivity
import xyz.joaophp.liftin.R
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.databinding.FragmentHomeBinding
import xyz.joaophp.liftin.ui.adapters.WorkoutAdapter
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.ui.viewmodels.HomeViewModel
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.DatabaseFailure
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.WorkoutFailure

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Handle Up button
        (requireActivity() as MainActivity).setOnBackPressedListener(null)

        runBlocking {
            // Check authentication
            viewModel.getUser().fold(
                ifError = { failure ->
                    handleFailure(failure)
                    navigateToAuthFragment()
                },
                ifSuccess = { user -> appViewModel.updateState(AppState.InHome(user)) }
            )
        }

        // Set up workout list adapter
        val workoutAdapter = WorkoutAdapter()
        workoutAdapter.addOnClickListener { workout ->
            navigateToWorkoutFragment(workout)
        }
        workoutAdapter.addOnDeleteClickListener { workout ->
            deleteWorkout(workout)
        }

        // Set up workout list
        binding?.rvWorkoutList?.adapter = workoutAdapter

        // Fab click listener
        binding?.apply {
            fab.setOnClickListener { navigateToAddWorkoutFragment() }
        }

        // Flow collectors
        setUpCollectors()

        // Enable SignOut button in the action bar
        setHasOptionsMenu(true)

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // Options menu methods

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_icon -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Action methods

    private fun deleteWorkout(workout: Workout) {
        lifecycleScope.launchWhenStarted {
            viewModel.deleteWorkout(user, workout).fold(
                ifError = { failure -> handleFailure(failure) },
                ifSuccess = { deletedWorkout ->
                    displayMessage("The workout ${deletedWorkout.nome} has been deleted")
                }
            )
        }
    }

    private fun signOut() {
        runBlocking {

            // Show loading animation
            binding?.loading?.show()
            binding?.clRoot?.alpha = 0.5f // lowers opacity during loading

            viewModel.signOut().fold(
                ifError = { failure -> handleFailure(failure) },
                ifSuccess = { navigateToAuthFragment() }
            )

            // Hide loading animation
            binding?.clRoot?.alpha = 1f // opacity goes back to normal
            binding?.loading?.hide()

        }
    }

    // Handle flows

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setUpCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.getWorkouts(user).collect { either ->
                either.fold(
                    ifError = { failure -> handleFailure(failure) },
                    ifSuccess = { workoutList ->
                        val adapter = binding?.rvWorkoutList?.adapter as WorkoutAdapter?
                        adapter?.submitList(workoutList)
                    }
                )
            }
        }
    }

    // App state handling

    private fun handleState(state: AppState?) {
        when (state) {
            is AppState.InHome -> user = state.user
            else -> Unit
        }
    }

    // Navigation methods

    private fun navigateToAuthFragment() {
        appViewModel.updateState(AppState.InAuth)
        findNavController().navigate(R.id.action_homeFragment_to_authFragment)
    }

    private fun navigateToAddWorkoutFragment() {
        appViewModel.updateState(AppState.InAddWorkout(user))
        findNavController().navigate(R.id.action_homeFragment_to_addWorkoutFragment)
    }

    private fun navigateToWorkoutFragment(workout: Workout) {
        appViewModel.updateState(AppState.InWorkout(user, workout))
        findNavController().navigate(R.id.action_homeFragment_to_workoutFragment)
    }

    // Error handling

    private fun handleFailure(failure: Failure) {
        failure.e?.printStackTrace()
        val message = when (failure) {
            is AuthFailure.NoCurrentUser -> return
            is AuthFailure.CantRetrieveUser -> getString(R.string.user_retrieval_fail)
            is WorkoutFailure.WrongModel -> getString(R.string.wrong_model)
            is DatabaseFailure.Timeout -> getString(R.string.timeout)
            is DatabaseFailure.InvalidQuery -> getString(R.string.invalid_query)
            is DatabaseFailure.Unavailable -> getString(R.string.unavailable)
            is DatabaseFailure.DataLost -> getString(R.string.data_lost)
            is DatabaseFailure.Unauthorised -> getString(R.string.unauthorised)
            is DatabaseFailure.NotFound -> getString(R.string.workouts_not_found)
            else -> getString(R.string.unknown_error)
        }
        displayMessage(message)
    }

    private fun displayMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }
}