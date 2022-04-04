package ru.iu3.electronicslab.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.iu3.electronicslab.ElectronicsItem
import ru.iu3.electronicslab.R
import ru.iu3.electronicslab.Utils
import ru.iu3.electronicslab.databinding.ViewPageBinding

class ViewPage : Fragment(R.layout.view_page) {

    private var binding: ViewPageBinding ?= null
    private var editDeviceButton: Button ?= null
    private var currentDevice: ElectronicsItem ?= null
    private var backCallback: OnBackPressedCallback ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backCallback = requireActivity().onBackPressedDispatcher.addCallback(this){
            val action = ViewPageDirections.actionViewFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val thisBinding = ViewPageBinding.bind(view)
        binding = thisBinding
        currentDevice = Utils().getDefaultDevice(context).fromJSON(arguments?.getString("electronicsParams").toString())
        binding?.title?.setText(context?.getString(R.string.viewingProperties)+currentDevice?.name)
        binding?.parameterContainer?.electronicsName?.setText(currentDevice?.name)
        binding?.parameterContainer?.electronicsCategory?.setText(currentDevice?.category)
        binding?.parameterContainer?.electronicsBrand?.setText(currentDevice?.brand)
        binding?.parameterContainer?.electronicsWeight?.setText(Utils().returnWeight(currentDevice?.weight)+context?.getString(R.string.kg))
        binding?.parameterContainer?.electronicsVoltage?.setText(Utils().returnVoltage(currentDevice?.voltage)+context?.getString(R.string.V))
        binding?.parameterContainer?.electronicsGuarantee?.setText(currentDevice?.guarantee)
        binding?.parameterContainer?.electronicsPrice?.setText(Utils().returnPrice(currentDevice?.price)+context?.getString(R.string.R))
        editDeviceButton = binding?.editBtn
        editDeviceButton?.setOnClickListener{
            val action = ViewPageDirections.actionViewFragmentToEditFragment()
            val device = currentDevice
            action.electronicsParams = device?.toJSON() ?: Utils().getDefaultDevice(context).toJSON()
            findNavController().navigate(action)
        }
    }

}