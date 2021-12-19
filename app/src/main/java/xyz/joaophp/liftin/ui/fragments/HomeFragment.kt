package xyz.joaophp.liftin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import xyz.joaophp.liftin.MainActivity
import xyz.joaophp.liftin.R
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.ui.viewmodels.HomeViewModel
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure

class HomeFragment : Fragment() {

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.getUser().fold(
            ifError = { failure -> handleFailure(failure) },
            ifSuccess = { user -> appViewModel.updateState(AppState.InHome(user)) }
        )
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun handleFailure(failure: Failure) {
        when (failure) {
            is AuthFailure.NoCurrentUser -> navigateToAuthFragment()
            is AuthFailure.CantRetrieveUser -> {
                (activity as MainActivity)
                    .showToast("There was a problem retrieving authentication information")
                navigateToAuthFragment()
            }
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
}