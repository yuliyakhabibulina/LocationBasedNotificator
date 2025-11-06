package com.sap.codelab.presentation.create

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.sap.codelab.R
import com.sap.codelab.databinding.FragmentCreateMemoBinding
import com.sap.codelab.utils.extensions.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.Intent
import android.provider.Settings
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.maps.model.LatLng
import com.sap.codelab.presentation.map.MapFragment

/**
 * The fragment for creating a new memo. Also it provides checking location permissions.
 */
@AndroidEntryPoint
class CreateMemoFragment : Fragment() {

    private var _binding: FragmentCreateMemoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateMemoViewModel by viewModels()

    /**
     *checks permissions and handle result.
     */
    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                navigateToMapFragment()
            } else {
                if (shouldShowSettingsDialog()) {
                    showSettingsDialog()
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.permissions_not_granted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    /**
     *array of required permissions.
     */
    private val requiredPermissions =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        observeViewModel()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Sets up listeners for the get location button and the map fragment result listener.
     */
    private fun setupListeners() = with(binding) {
        buttonGetLocation.setOnClickListener {
            handlePermissionsRequest()
        }

        setFragmentResultListener(MapFragment.REQUEST_KEY_LOCATION) { _, bundle ->
            val location = bundle.getParcelable<LatLng>(MapFragment.BUNDLE_KEY_LOCATION)
            location?.let { viewModel.onLocationSelected(it) }
        }
    }

    /**
     * observes flow from view model and handle events.
     */
    private fun observeViewModel() = with(viewModel) {
        collectFlow(navBackEvent) { shouldNavigateBack ->
            if (shouldNavigateBack) {
                findNavController().popBackStack()
            }
        }
        collectFlow(errorMessageId) { error ->
            Toast.makeText(
                requireContext(),
                getString(error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Sets up the menu for the fragment.
     */
    private fun setupMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(
                    menu: Menu,
                    menuInflater: MenuInflater
                ) {
                    menuInflater.inflate(R.menu.menu_create_memo, menu)
                }

                override fun onMenuItemSelected(item: MenuItem): Boolean {
                    return when (item.itemId) {
                        R.id.action_save -> {
                            viewModel.onSaveMenuClicked(
                                binding.memoTitle.text.toString(),
                                binding.memoDescription.text.toString()
                            )
                            true
                        }

                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    /**
     * Handles the permissions request. Show permission request dialog if needed.
     */
    private fun handlePermissionsRequest() {
        if (shouldShowRationaleDialog()) {
            showRationaleDialog()
        } else {
            permissionsLauncher.launch(requiredPermissions)
        }
    }

    /**
     * Checks if we should show a rationale dialog.
     */
    private fun shouldShowRationaleDialog(): Boolean {
        return requiredPermissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), it)
        }
    }

    /**
     * Checks if we should show a setting dialog.
     */
    private fun shouldShowSettingsDialog(): Boolean {
        return !shouldShowRationaleDialog()
    }

    /**
     * Shows a dialog for requesting permissions.
     */
    private fun showRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.permission_dialog_rationale_title)
            .setMessage(R.string.permission_dialog_rationale_message)
            .setPositiveButton(R.string.button_ok) { _, _ ->
                permissionsLauncher.launch(requiredPermissions)
            }
            .setNegativeButton(R.string.button_cancel, null)
            .show()
    }

    /**
     * Shows a dialog that leads to the app settings.
     */
    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.permission_dialog_settings_title)
            .setMessage(R.string.permission_dialog_settings_message)
            .setPositiveButton(R.string.button_settings) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(R.string.button_cancel, null)
            .show()
    }

    /**
     * Provides navigation to the map fragment.
     */
    private fun navigateToMapFragment() {
        findNavController().navigate(R.id.action_createMemoFragment_to_mapFragment)
    }
}