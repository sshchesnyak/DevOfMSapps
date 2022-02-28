package ru.iu3.numlab

import androidx.recyclerview.widget.DiffUtil

class NumberDiffCallback(
    private val oldSet: MutableList<NumberItem>,
    private val newSet: MutableList<NumberItem>)
    : DiffUtil.Callback() {
        override fun getOldListSize() = oldSet.size
        override fun getNewListSize() = newSet.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSet[oldItemPosition].number==newSet[newItemPosition].number
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSet[oldItemPosition].createSubtitle()==newSet[newItemPosition].createSubtitle()
        }
    }