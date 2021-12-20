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
import xyz.joaophp.liftin.R
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.databinding.FragmentAuthBinding
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.ui.viewmodels.AuthViewModel
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var binding: FragmentAuthBinding? = null

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        binding?.apply {

            btnSignIn.setOnClickListener {
                lifecycleScope.launchWhenCreated {
                    val email = tfEmail.text?.toString() ?: "jaime"
                    val password = tfPassword.text?.toString() ?: "jaime"

                    loading.show()
                    root.alpha = 0.5f

                    viewModel.login(email, password).fold(
                        ifError = { failure -> handleFailure(failure) },
                        ifSuccess = { user -> navigateToHomeFragment(user) }
                    )

                    root.alpha = 1f
                    loading.hide()
                }
            }

            btnRegister.setOnClickListener {
                lifecycleScope.launchWhenCreated {
                    val email = tfEmail.text?.toString() ?: "jaime"
                    val password = tfPassword.text?.toString() ?: "jaime"

                    loading.show()
                    root.alpha = 0.5f

                    viewModel.register(email, password).fold(
                        ifError = { failure -> handleFailure(failure) },
                        ifSuccess = { user -> navigateToHomeFragment(user) }
                    )

                    root.alpha = 1f
                    loading.hide()
                }

            }
        }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // Navigation methods

    private fun navigateToHomeFragment(user: User) {
        appViewModel.updateState(AppState.InHome(user))
        findNavController().navigate(R.id.action_authFragment_to_homeFragment)
    }

    // Error handling

    private fun handleFailure(failure: Failure) {
        when (failure) {
            is AuthFailure.BadEmail ->
                displayError("The email is invalid")
            is AuthFailure.InvalidCredentials ->
                displayError("Invalid Credentials")
            is AuthFailure.InvalidUser ->
                displayError("This email is not registered to any account")
            is AuthFailure.EmailTaken ->
                displayError("The email is already in use")
            is AuthFailure.CantRetrieveUser ->
                displayError("There was a problem retrieving authentication information\n${failure.e}")
            is AuthFailure.WeakPassword ->
                displayError("${failure.reason}")
            else ->
                displayError("An unexpected error has occurred:\n${failure.e}")
        }
    }

    private fun displayError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

}