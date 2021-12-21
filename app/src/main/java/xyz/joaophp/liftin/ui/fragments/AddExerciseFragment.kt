package xyz.joaophp.liftin.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.databinding.FragmentAddExerciseBinding
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AddExerciseViewModel
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.utils.failures.Failure
import xyz.joaophp.liftin.utils.failures.ImageFailure
import xyz.joaophp.liftin.utils.failures.WorkoutFailure


@AndroidEntryPoint
class AddExerciseFragment : Fragment() {

    private var binding: FragmentAddExerciseBinding? = null

    private val appViewModel: AppViewModel by activityViewModels()
    private val viewModel: AddExerciseViewModel by viewModels()

    private lateinit var user: User
    private lateinit var workout: Workout
    private var imageUri: Uri? = null

    private lateinit var requestPermission: ActivityResultLauncher<String>
    private lateinit var getContent: ActivityResultLauncher<String>

    // Lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    getContent.launch("image/*")
                } else {
                    displayMessage("You need to give permission for access to the gallery")
                }
            }
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
            if (contentUri == null) {
                handleFailure(ImageFailure.EmptyUri)
            } else {
                imageUri = contentUri
                Picasso.get().load(contentUri).into(binding?.ivImage)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExerciseBinding.inflate(inflater, container, false)

        // Handle state
        lifecycleScope.launchWhenStarted {
            appViewModel.appState.collect { state -> handleState(state) }
        }

        // Add Image button listener
        binding?.btnAddImage?.setOnClickListener { getImageFromGallery() }

        // Fab click listener
        binding?.fab?.setOnClickListener { createExercise() }

        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    // Action methods

    private fun getImageFromGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getContent.launch("image/*")
        } else {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun createExercise() {

        // Show loading animation
        binding?.loading?.show()
        binding?.root?.alpha = 0.5f // lowers opacity during loading

        // Get input fields values
        val nome = binding?.tfNome?.text?.toString()
        val observacoes = binding?.tfObservacao?.text?.toString()

        // Check if they are null or empty
        if (nome.isNullOrEmpty() || observacoes.isNullOrEmpty()) {

            // Hide loading animation
            binding?.root?.alpha = 1f // opacity goes back to normal
            binding?.loading?.hide()

            return handleFailure(WorkoutFailure.EmptyFields)
        }

        // Create exercise
        lifecycleScope.launchWhenCreated {
            imageUri?.let { uri ->
                viewModel.createImage(user, uri).foldAsync(
                    ifError = { failure -> handleFailure(failure) },
                    ifSuccess = { path ->
                        Log.d("TESTE", path)
                        viewModel.createExercise(
                            user,
                            workout,
                            nome.toInt(),
                            observacoes,
                            path
                        ).fold(
                            ifError = { failure -> handleFailure(failure) },
                            ifSuccess = { navigateToHomeFragment() }
                        )
                    }
                )
            }
        }

        // Hide loading animation
        binding?.root?.alpha = 1f // opacity goes back to normal
        binding?.loading?.hide()
    }

    // Handle AppState

    private fun handleState(state: AppState?) {
        when (state) {
            is AppState.InAddExercise -> {
                user = state.user
                workout = state.workout
            }
            else -> Unit
        }
    }

    // Navigation methods

    private fun navigateToHomeFragment() {
        appViewModel.updateState(AppState.InWorkout(user, workout))
        findNavController().navigateUp()
    }

    // Handle errors

    private fun handleFailure(failure: Failure) {
        when (failure) {
            else -> {
                failure.e?.printStackTrace()
                displayMessage("An unknown error has happened.\nContact the developer.")
            }
        }
    }

    private fun displayMessage(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

}