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
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.AuthFailure
import xyz.joaophp.liftin.utils.failures.Failure

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var binding: FragmentAuthBinding? = null

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: AuthViewModel by viewModels()

    // Lifecycle methods

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        // Click listeners
        binding?.btnSignIn?.setOnClickListener { authenticate(viewModel::login) }
        binding?.btnRegister?.setOnClickListener { authenticate(viewModel::register) }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // Action methods

    private fun authenticate(
        authMethod: suspend (email: String, password: String) -> Either<Failure, User>
    ) {
        binding?.run {
            lifecycleScope.launchWhenCreated {

                // Get email and password from input fields
                val email = tfEmail.text?.toString()
                val password = tfPassword.text?.toString()

                // Check if credentials are not empty or null
                if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    handleFailure(AuthFailure.EmptyCredentials)
                    return@launchWhenCreated
                }

                // Show loading animation
                loading.show()
                clRoot.alpha = 0.5f // lowers opacity during loading

                // Authenticate
                authMethod(email, password).fold(
                    ifError = { failure -> handleFailure(failure) },
                    ifSuccess = { user -> navigateToHomeFragment(user) }
                )

                // Hide loading animation
                clRoot.alpha = 1f // opacity goes back to normal
                loading.hide()
            }
        }
    }

    // Navigation methods

    private fun navigateToHomeFragment(user: User) {
        appViewModel.updateState(AppState.InHome(user))
        findNavController().navigate(R.id.action_authFragment_to_homeFragment)
    }

    // Error handling

    private fun handleFailure(failure: Failure) {
        failure.e?.printStackTrace()
        val message = when (failure) {
            is AuthFailure.EmptyCredentials -> getString(R.string.empty_credentials)
            is AuthFailure.BadEmail -> getString(R.string.bad_email)
            is AuthFailure.InvalidCredentials -> getString(R.string.invalid_credentials)
            is AuthFailure.InvalidUser -> getString(R.string.invalid_email)
            is AuthFailure.EmailTaken -> getString(R.string.email_taken)
            is AuthFailure.CantRetrieveUser -> getString(R.string.user_retrieval_fail)
            is AuthFailure.WeakPassword -> getString(R.string.weak_password)
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