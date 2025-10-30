package com.sap.codelab.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var menuItemShowAll: MenuItem
    private lateinit var menuItemShowOpen: MenuItem

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadOpenMemos()
        setupMenu()
        setupRecyclerView()
        observeViewmodel()
        setupFab()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Observe viewModel
     */
    private fun observeViewmodel() = with(viewModel){
        collectFlow(memos) { memos ->
            memoAdapter.submitList(memos)
        }
    }

    /**
     * Initializes the recycler view to display the list of memos.
     */
    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = memoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_createMemoFragment)
        }
    }

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

    private fun switchMenu(flag: Boolean) {
        menuItemShowAll.isVisible = !flag
        menuItemShowOpen.isVisible = flag
    }

}