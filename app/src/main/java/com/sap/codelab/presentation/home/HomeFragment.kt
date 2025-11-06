package com.sap.codelab.presentation.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sap.codelab.R
import com.sap.codelab.databinding.FragmentHomeBinding
import com.sap.codelab.utils.extensions.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

/**
 * The fragment for showing a list of memos. Also it provides checking notification permissions.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var menuItemShowAll: MenuItem
    private lateinit var menuItemShowOpen: MenuItem

    /**
     *checks permissions and handle result.
     */
    private val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!isNotificationPermissionGranted(requireContext())) {
                showNotificationPermissionDialog()
            }
        }

    /**
     *required permission.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requiredPermissions =  Manifest.permission.POST_NOTIFICATIONS

    /**
     * adapter for memo list.
     */
    private val memoAdapter: MemoAdapter by lazy {
        MemoAdapter(
            onMemoClick = { item ->
                val action =
                    HomeFragmentDirections.actionNavHomeFragmentToNavMemoDetailsFragment(item)
                findNavController().navigate(action)
            },

            onCheckedChange = { memo, isChecked ->
                viewModel.updateMemo(memo, isChecked)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadOpenMemos()
        setupMenu()
        setupView()
        observeViewmodel()
        permissionsLauncher.launch(requiredPermissions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewmodel() = with(viewModel){
        collectFlow(memos) { memos ->
            memoAdapter.submitList(memos)
        }
    }

    /**
     *sets up recycler view and fab button.
     */
    private fun setupView() = with(binding) {
        recyclerView.apply {
            adapter = memoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
        fab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_createMemoFragment)
        }
    }

    /**
     *sets up menu for fragment.
     */
    private fun setupMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(
                    menu: Menu,
                    menuInflater: MenuInflater
                ) {
                    menuInflater.inflate(R.menu.menu_home, menu)
                    menuItemShowAll = menu.findItem(R.id.action_show_all)
                    menuItemShowOpen = menu.findItem(R.id.action_show_open)
                }

                override fun onMenuItemSelected(item: MenuItem): Boolean {
                    return when (item.itemId) {
                        R.id.action_show_all -> {
                            viewModel.loadAllMemos()
                            switchMenu(true)
                            true
                        }

                        R.id.action_show_open -> {
                            viewModel.loadOpenMemos()
                            switchMenu(false)
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
     *switches menu items visibility.
     */
    private fun switchMenu(isVisible: Boolean) {
        menuItemShowAll.isVisible = !isVisible
        menuItemShowOpen.isVisible = isVisible
    }

    /**
     *shows dialog for notification permission.
     */
    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.permission_dialog_rationale_title)
            .setMessage(R.string.permission_dialog_rationale_message_notification)
            .setPositiveButton(R.string.permission_dialog_settings_positive_button)  { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(R.string.permission_dialog_rationale_negative_button, null)
            .show()
    }

    /**
     *checks notification permission.
     */
    fun isNotificationPermissionGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}