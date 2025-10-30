package com.sap.codelab.view.create

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sap.codelab.R
import com.sap.codelab.databinding.FragmentCreateMemoBinding
import com.sap.codelab.utils.extensions.getEmptyString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateMemoFragment : Fragment() {

    private var _binding: FragmentCreateMemoBinding ? = null
    private val binding get() = _binding!!

    private val viewModel: CreateMemoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateMemoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_create_memo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveMemo()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Saves the memo if the input is valid; otherwise shows the corresponding error messages.
     */
    private fun saveMemo() {
        binding.run {
            viewModel.updateMemo(memoTitle.text.toString(), memoDescription.text.toString())
            if (viewModel.isMemoValid()) {
                viewModel.saveMemo()
                findNavController().popBackStack()
            } else {
                memoTitleContainer.error =
                    getErrorMessage(viewModel.hasTitleError(), R.string.memo_title_empty_error)
                memoDescriptionContainer.error =
                    getErrorMessage(viewModel.hasTextError(), R.string.memo_text_empty_error)
            }
        }
    }

    /**
     * Returns the error message if there is an error, or an empty string otherwise.
     *
     * @param hasError          - whether there is an error.
     * @param errorMessageResId - the resource id of the error message to show.
     * @return the error message if there is an error, or an empty string otherwise.
     */
    private fun getErrorMessage(hasError: Boolean, @StringRes errorMessageResId: Int): String {
        return if (hasError) {
            getString(errorMessageResId)
        } else {
            getEmptyString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}