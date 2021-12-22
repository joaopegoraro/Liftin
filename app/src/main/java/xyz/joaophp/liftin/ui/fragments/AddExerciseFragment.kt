package xyz.joaophp.liftin.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import xyz.joaophp.liftin.MainActivity
import xyz.joaophp.liftin.R
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.data.models.Workout
import xyz.joaophp.liftin.databinding.FragmentAddExerciseBinding
import xyz.joaophp.liftin.ui.state.AppState
import xyz.joaophp.liftin.ui.viewmodels.AddExerciseViewModel
import xyz.joaophp.liftin.ui.viewmodels.AppViewModel
import xyz.joaophp.liftin.utils.failures.*


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

        // Handle state
        lifecycleScope.launchWhenCreated {
            appViewModel.appState.collect { state -> handleState(state) }
        }

        // Request permission INTENT
        requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    getContent.launch("image/*")
                } else {
                    handleFailure(ImageFailure.PermissionNeeded)
                }
            }

        // Get Image file INTENT
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
            contentUri?.let {
                imageUri = contentUri
                Picasso.get().load(imageUri).into(binding?.ivImage)
            }
        }

        // Navigation transition
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExerciseBinding.inflate(inflater, container, false)

        // Handle Up button
        (requireActivity() as MainActivity).setOnBackPressedListener {
            navigateToWorkoutFragment()
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
        binding?.apply {

            // Get input fields values
            val nome = tfNome.text.toString()
            val observacoes = tfObservacao.text.toString()

            // Check if they are empty
            if (nome.isEmpty() || observacoes.isEmpty()) {
                return handleFailure(ExerciseFailure.EmptyFields)
            }

            // Create exercise
            lifecycleScope.launchWhenResumed {

                // Show loading animation
                loading.show()
                clRoot.alpha = 0.5f // lowers opacity during loading

                imageUri?.let { uri ->
                    viewModel.createImage(user, uri).foldAsync(
                        ifError = { failure -> handleFailure(failure) },
                        ifSuccess = { path ->
                            viewModel.createExercise(
                                user,
                                workout,
                                nome.toInt(),
                                observacoes,
                                path
                            ).fold(
                                ifError = { failure -> handleFailure(failure) },
                                ifSuccess = { navigateToWorkoutFragment() }
                            )
                        }
                    )
                    return@launchWhenResumed
                }

                // Hide loading animation
                clRoot.alpha = 1f // opacity goes back to normal
                loading.hide()

                handleFailure(ImageFailure.EmptyField)
            }
        }
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

    private fun navigateToWorkoutFragment() {
        appViewModel.updateState(AppState.InWorkout(user, workout))
        findNavController().navigateUp()
    }

    // Handle errors

    private fun handleFailure(failure: Failure) {
        failure.e?.printStackTrace()
        val message = when (failure) {
            is ExerciseFailure.WrongModel -> getString(R.string.wrong_model)
            is ExerciseFailure.EmptyFields -> getString(R.string.exercise_empty_fields)
            is ImageFailure.EmptyField -> getString(R.string.image_empty_field)
            is ImageFailure.PermissionNeeded -> getString(R.string.permission_needed)
            is StorageFailure.Unauthorised -> getString(R.string.unauthorised)
            is StorageFailure.Timeout -> getString(R.string.timeout)
            is StorageFailure.LimitExceeded -> getString(R.string.upload_limit_exceeded)
            is DatabaseFailure.InvalidQuery -> getString(R.string.invalid_query)
            is DatabaseFailure.Timeout -> getString(R.string.timeout)
            is DatabaseFailure.Unauthorised -> getString(R.string.unauthorised)
            is DatabaseFailure.DocumentAlreadyExists -> getString(R.string.exercise_already_exists)
            is DatabaseFailure.Unavailable -> getString(R.string.unavailable)
            is DatabaseFailure.DataLost -> getString(R.string.data_lost)
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