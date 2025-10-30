package com.sap.codelab.view.create

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu

import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.sap.codelab.R
import com.sap.codelab.databinding.ActivityCreateMemoBinding
import com.sap.codelab.utils.extensions.getEmptyString
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity that allows a user to create a new Memo.
 */
@AndroidEntryPoint
class CreateMemoActivity : AppCompatActivity() {

    private var _binding: ActivityCreateMemoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateMemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCreateMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_create_memo, menu)
        return true
    }

    /**
     * Handles actionbar interactions.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveMemo()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
    /**
     * Saves the memo if the input is valid; otherwise shows the corresponding error messages.
     */
    private fun saveMemo() {
        binding.contentCreateMemo.run {
            viewModel.updateMemo(memoTitle.text.toString(), memoDescription.text.toString())
            if (viewModel.isMemoValid()) {
                viewModel.saveMemo()
                setResult(RESULT_OK)
                finish()
            } else {
                memoTitleContainer.error =
                    getErrorMessage(viewModel.hasTitleError(), R.string.memo_title_empty_error)
                memoDescription.error =
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
}
