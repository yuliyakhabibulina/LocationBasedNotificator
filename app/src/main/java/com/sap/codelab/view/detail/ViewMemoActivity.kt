package com.sap.codelab.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.sap.codelab.databinding.ActivityViewMemoBinding
import com.sap.codelab.model.Memo
import kotlinx.coroutines.launch

internal const val BUNDLE_MEMO_ID: String = "memoId"

/**
 * Activity that allows a user to see the details of a memo.
 */
internal class ViewMemoActivity : AppCompatActivity() {

    private var _binding: ActivityViewMemoBinding? = null
    private val binding get() = _binding!!

    private val model: ViewMemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        _binding = ActivityViewMemoBinding.inflate(layoutInflater)
        val id = intent.getLongExtra(BUNDLE_MEMO_ID, -1)

        model.loadMemo(id)

          if (savedInstanceState == null) {
            // Observe the memo state flow for changes
            lifecycleScope.launch {
                model.memo.collect { value ->
                    value?.let { memo ->
                        // Update the UI whenever the memo changes
                        updateUI(memo)
                    }
                }
            }


        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * Updates the UI with the given memo details.
     *
     * @param memo - the memo whose details are to be displayed.
     */
    private fun updateUI(memo: Memo) {
        binding.contentCreateMemo.run {
            memoTitle.setText(memo.title)
            memoDescription.setText(memo.description)
            memoTitle.isEnabled = false
            memoDescription.isEnabled = false
        }
    }
}
