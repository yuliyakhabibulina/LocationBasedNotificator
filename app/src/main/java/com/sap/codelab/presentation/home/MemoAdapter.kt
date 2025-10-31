package com.sap.codelab.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.sap.codelab.databinding.ItemMemoBinding
import com.sap.codelab.presentation.model.MemoUI

/**
 * Adapter containing a set of memos.
 */
class MemoAdapter(
    private val onMemoClick: (Long) -> Unit,
    private val onCheckedChange: (MemoUI, Boolean) -> Unit
) : ListAdapter<MemoUI, MemoViewHolder>(MemoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val binding = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemoViewHolder(binding, onMemoClick, onCheckedChange)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MemoDiffCallback : DiffUtil.ItemCallback<MemoUI>() {
    override fun areItemsTheSame(oldItem: MemoUI, newItem: MemoUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MemoUI, newItem: MemoUI): Boolean {
        return oldItem == newItem
    }
}