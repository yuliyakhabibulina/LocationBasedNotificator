package com.sap.codelab.view.home

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sap.codelab.R
import com.sap.codelab.databinding.ContentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: ContentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var menuItemShowAll: MenuItem
    private lateinit var menuItemShowOpen: MenuItem

    private val memoAdapter: MemoAdapter by lazy {
        MemoAdapter(
            onMemoClick = viewModel::onMemoClicked,
            onCheckedChange = viewModel::onMemoCheckedChanged
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        viewModel.loadOpenMemos()
        // Setup the adapter and the recycler view
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
    private fun observeViewmodel() {
        lifecycle.coroutineScope.launch {
            viewModel.memos.collect { memos ->
                memoAdapter.submitList(memos)
            }
        }
    }
    /**
     * Initializes the recycler view to display the list of memos.
     */
    private fun setupRecyclerView()  {
        binding.recyclerView.apply {
            adapter = memoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            // Handles clicks on the FAB button > creates a new Memo
            it.findNavController().navigate(R.id.action_homeFragment_to_createMemoFragment)
        }
    }

    /**
     * Opens the Memo detail view for the given memoId.
     *
     * @param memoId    - the id of the memo to be shown.
     */
    private fun showMemo(memoId: Long) {
//        val intent = Intent(activity, ViewMemoActivity::class.java)
//        intent.putExtra(BUNDLE_MEMO_ID, memoId)
//        startActivity(intent)
    }




    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu) // Use the inflater parameter
        menuItemShowAll = menu.findItem(R.id.action_show_all)
        menuItemShowOpen = menu.findItem(R.id.action_show_open)
    }

    /**
     * Handles actionbar interactions.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_all -> {
                viewModel.loadAllMemos()
                //Switch available menu options
                menuItemShowAll.isVisible = false
                menuItemShowOpen.isVisible = true
                true
            }
            R.id.action_show_open -> {
                viewModel.loadOpenMemos()
                //Switch available menu options
                menuItemShowOpen.isVisible = false
                menuItemShowAll.isVisible = true
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}