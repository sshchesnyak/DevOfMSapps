package ru.iu3.noteslab

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import ru.iu3.noteslab.databinding.NoteCellBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(private var myNotes: MutableList<NoteItem>,
                  private val onShortClicked: (item: NoteItem)->Unit):RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    inner class ViewHolder(override val containerView: View,
                           private var Note: NoteCellBinding,
                           private val onShort: (item: NoteItem) -> Unit,
                           private val context: Context):RecyclerView.ViewHolder(Note.root),
    LayoutContainer,View.OnLongClickListener, View.OnClickListener{
        private var currentNote:NoteItem ?= null
        private val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        init{
            Note.noteContainer.setOnClickListener(this)
            Note.noteContainer.setOnLongClickListener(this)
        }

        fun bind(note:NoteItem) = with(containerView){
            currentNote = note
            Note.cellTitle.text = note.title
            Note.cellDate.text = formatter.format(note.date)
        }

        override fun onClick(view: View?) {
            currentNote?.let(onShort)
        }

        override fun onLongClick(view: View?): Boolean {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.getString(R.string.alertTitle))
                .setMessage(context.getString(R.string.alertBody))
                .setNegativeButton(context.getString(R.string.no)) { dialog, _ -> dialog.cancel() }
                .setPositiveButton(context.getString(R.string.yes))
                { dialog, _ ->
                    run {
                        val file = Utils().getFile(context)
                        val notes: MutableList<NoteItem> = Utils().getAllNotes(context)
                        val index = notes.indexOf(currentNote)

                        if (index != -1) notes.removeAt(index)
                        val bufferedWriter = file.bufferedWriter()
                        for (i in 0 until notes.size) bufferedWriter.write(notes[i].toJSON() + "\n")
                        bufferedWriter.close()

                        dispatchUpdates(notes)
                        dialog.cancel()
                    }
                }
            builder.create()
            builder.show()
            return true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val numberView = inflater.inflate(R.layout.note_cell,parent,false)
        val inflateNumberItem = NoteCellBinding.inflate(inflater,parent,false)
        return ViewHolder(numberView,inflateNumberItem,onShortClicked,parent.context)
    }

    override fun onBindViewHolder(viewHolder: NoteAdapter.ViewHolder, position: Int) {
        viewHolder.bind(myNotes[position])
    }

    override fun getItemCount(): Int {
        return myNotes.size
    }

    fun dispatchUpdates(newNotes: MutableList<NoteItem>){
        val diffCallback = NoteDiffCallback(this.myNotes, newNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.myNotes = newNotes
        diffResult.dispatchUpdatesTo(this)
    }
}