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
import xyz.joaophp.liftin.R
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.databinding.FragmentHomeBinding
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.ui.viewmodels.HomeViewModel
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var user: User

    // Lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check authentication
        viewModel.getUser().fold(
            ifError = { failure ->
                handleFailure(failure)
                navigateToAuthFragment()
            },
            ifSuccess = { user -> appViewModel.updateState(AppState.InHome(user)) }
        )

        // Handle state
        lifecycleScope.launchWhenStarted {
            appViewModel.appState.collect { state -> handleState(state) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding?.apply {
            fab.setOnClickListener { navigateToAddWorkoutFragment() }
        }

        setUpCollectors()

        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_icon -> signOut()
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Action methods

    private fun signOut(): Boolean {

        // Show loading animation
        binding?.loading?.show()
        binding?.root?.alpha = 0.5f // lowers opacity during loading

        viewModel.signOut().fold(
            ifError = { failure -> handleFailure(failure) },
            ifSuccess = { navigateToAuthFragment() }
        )

        // Hide loading animation
        binding?.root?.alpha = 1f // opacity goes back to normal
        binding?.loading?.hide()

        return true
    }

    // Handle flows

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setUpCollectors() {
        lifecycleScope.launchWhenStarted {
            viewModel.getWorkouts(user)
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
        appViewModel.updateState(AppState.InAuth(null))
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
        when (failure) {
            is AuthFailure.NoCurrentUser -> Unit
            is AuthFailure.CantRetrieveUser -> {
                displayError("There was a problem retrieving authentication information")
                navigateToAuthFragment()
            }
            else -> displayError("An unexpected error has occured\n" + failure.e)
        }
    }

    private fun displayError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }
}