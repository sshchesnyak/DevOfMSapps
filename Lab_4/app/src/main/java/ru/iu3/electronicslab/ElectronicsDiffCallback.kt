package ru.iu3.electronicslab

import androidx.recyclerview.widget.DiffUtil

class ElectronicsDiffCallback(
    private val oldSet: MutableList<ElectronicsItem>,
    private val newSet: MutableList<ElectronicsItem>):DiffUtil.Callback() {
        override fun getOldListSize() = oldSet.size
        override fun getNewListSize() = newSet.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSet[oldItemPosition].id==newSet[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldSet[oldItemPosition].toJSON()==newSet[newItemPosition].toJSON()
        }
}