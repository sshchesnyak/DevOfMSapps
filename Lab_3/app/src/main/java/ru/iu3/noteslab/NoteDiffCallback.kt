package ru.iu3.noteslab

import androidx.recyclerview.widget.DiffUtil

class NoteDiffCallback(
    private val oldSet: MutableList<NoteItem>,
    private val newSet: MutableList<NoteItem>
    ):DiffUtil.Callback() {
    override fun getOldListSize() = oldSet.size
    override fun getNewListSize() = newSet.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldSet[oldItemPosition].id==newSet[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldSet[oldItemPosition].toJSON()==newSet[newItemPosition].toJSON()
    }
}