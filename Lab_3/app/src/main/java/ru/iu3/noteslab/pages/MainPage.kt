package ru.iu3.noteslab.pages

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.iu3.noteslab.NoteAdapter
import ru.iu3.noteslab.R
import ru.iu3.noteslab.Utils
import ru.iu3.noteslab.databinding.MainBinding

class MainPage : Fragment(R.layout.main) {

    private var binding: MainBinding?=null
    private var addNoteButton: Button?=null
    private var sortNoteButton: Button?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val thisBinding = MainBinding.bind(view)
        binding=thisBinding
        val noteAdapter = initRecycler()
        addNoteButton = binding?.recyclerButtonContainer?.addNote
        addNoteButton?.setOnClickListener{
            val action = MainPageDirections.actionMainFragmentToNoteFragment()
            val note = Utils().getDefaultNote(context)
            action.noteContents = note.toJSON()
            findNavController().navigate(action)
        }
        val notes = Utils().getAllNotes(context)
        sortNoteButton = binding?.recyclerButtonContainer?.sortNotes
        sortNoteButton?.setOnClickListener{
            Utils().sortText(notes)
            noteAdapter.dispatchUpdates(notes)
            Utils().writeToFile(Utils().getFile(context),notes)
        }
    }

    private fun initRecycler():NoteAdapter{
        val noteList = Utils().getAllNotes(context)
        val noteAdapter = NoteAdapter(noteList,
            onShortClicked = {
                val action = MainPageDirections.actionMainFragmentToNoteFragment()
                val note = it
                action.noteContents = note.toJSON()
                findNavController().navigate(action)
            })
        val recNoteView = binding?.recyclerButtonContainer?.noteRecycler as RecyclerView
        recNoteView.adapter = noteAdapter
        recNoteView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        recNoteView.addItemDecoration(DividerItemDecoration(context,RecyclerView.VERTICAL))
        return noteAdapter
    }

}