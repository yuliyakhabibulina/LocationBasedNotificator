package com.sap.codelab.presentation.viewMemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sap.codelab.databinding.FragmentViewMemoBinding
import com.sap.codelab.domain.model.Memo
import com.sap.codelab.utils.extensions.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class ViewMemoFragment : Fragment() {

    private var _binding: FragmentViewMemoBinding? = null
    private val binding get() = _binding!!
    private val args: ViewMemoFragmentArgs by navArgs()
    private val viewModel: ViewMemoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadMemo(args.memoId)
        }
        observeViewModel()
    }

    private fun observeViewModel() = with(viewModel){
        collectFlow(memo) { value ->
            value?.let { memo ->
                updateUI(memo)
            }
        }
    }

    /**
     * Updates the UI with the given memo details.
     *
     * @param memo - the memo whose details are to be displayed.
     */
    private fun updateUI(memo: Memo) = with(binding) {
        memoTitle.setText(memo.title)
        memoDescription.setText(memo.description)
        memoTitle.isEnabled = false
        memoDescription.isEnabled = false

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}