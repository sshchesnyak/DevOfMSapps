package ru.iu3.electronicslab.pages

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.iu3.electronicslab.ElectronicsAdapter
import ru.iu3.electronicslab.R
import ru.iu3.electronicslab.Utils
import ru.iu3.electronicslab.databinding.MainPageBinding

class MainPage: Fragment(R.layout.main_page) {

    private var binding: MainPageBinding?=null
    private var addDeviceButton: Button?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val thisBinding = MainPageBinding.bind(view)
        binding=thisBinding
        initRecycler()
        addDeviceButton = binding?.recyclerButtonContainer?.addDevice
        addDeviceButton?.setOnClickListener{
            val action = MainPageDirections.actionMainFragmentToEditFragment()
            val device = Utils().getDefaultDevice(context)
            action.electronicsParams = device.toJSON()
            findNavController().navigate(action)
        }
    }

    private fun initRecycler(){
        val deviceList = Utils().getAllDevices(context)
        val deviceAdapter = ElectronicsAdapter(deviceList,
            onShortClicked = {
                val action = MainPageDirections.actionMainFragmentToViewFragment()
                val device = it
                action.electronicsParams = device.toJSON()
                findNavController().navigate(action)
            })
        val recNoteView = binding?.recyclerButtonContainer?.electronicsRecycler as RecyclerView
        recNoteView.adapter = deviceAdapter
        recNoteView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        recNoteView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    }
}