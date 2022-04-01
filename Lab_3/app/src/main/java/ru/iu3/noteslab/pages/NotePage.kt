package ru.iu3.noteslab.pages

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.iu3.noteslab.NoteItem
import ru.iu3.noteslab.R
import ru.iu3.noteslab.Utils
import ru.iu3.noteslab.databinding.NoteBinding

class NotePage : Fragment(R.layout.note) {

    private var binding: NoteBinding ?= null
    private var saveNoteButton: Button ?= null
    private var currentObject: NoteItem ?= null
    private var backCallback: OnBackPressedCallback ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backCallback = requireActivity().onBackPressedDispatcher.addCallback(this){
            saveContents()
            val action = NotePageDirections.actionNoteFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val thisBinding = NoteBinding.bind(view)
        binding = thisBinding
        //Working with the view
        currentObject = Utils().getDefaultNote(context).fromJSON(arguments?.getString("noteContents").toString())
        binding?.noteTitle?.setText(currentObject?.title)
        binding?.noteContent?.setText(currentObject?.content)
        saveNoteButton = binding?.saveBtn
        saveNoteButton?.setOnClickListener{
            saveContents()
            val action = NotePageDirections.actionNoteFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

    private fun saveContents(){

        //check if content was edited
        val unedited = (currentObject?.title == binding?.noteTitle?.text.toString()) and (currentObject?.content == binding?.noteContent?.text.toString())

        if (!unedited){
            //initializing variables
            val file = Utils().getFile(context)
            val notes: MutableList<NoteItem> = Utils().getAllNotes(context)
            val index = notes.indexOf(currentObject)

            //actualizing data
            if (binding?.noteTitle?.text.toString()=="") currentObject?.title = getString(R.string.defaultNoteTitle)
            else currentObject?.title = binding?.noteTitle?.text.toString()
            currentObject?.content = binding?.noteContent?.text.toString()
            currentObject?.date = Utils().getCurrentDate(context)

            //if element is found within the list, then we have to remove it before
            //adding to the top of the list (editing an element)
            if (index!=-1) notes.removeAt(index)
            //adding an element to the top of the list (creating new element)
            notes.add(0, currentObject ?: Utils().getDefaultNote(context))
            val bufferedWriter = file.bufferedWriter()
            for (i in 0 until notes.size) bufferedWriter.write(notes[i].toJSON()+"\n")
            bufferedWriter.close()
        }

        //clearing space before quitting
        binding?.noteTitle?.setText("")
        binding?.noteContent?.setText("")
    }

}