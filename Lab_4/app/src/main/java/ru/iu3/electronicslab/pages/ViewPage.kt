package ru.iu3.electronicslab.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.iu3.electronicslab.ElectronicsItem
import ru.iu3.electronicslab.ElectronicsViewModel
import ru.iu3.electronicslab.R
import ru.iu3.electronicslab.Utils
import ru.iu3.electronicslab.databinding.ViewPageBinding

class ViewPage : Fragment(R.layout.view_page) {

    private var binding: ViewPageBinding ?= null
    private var editDeviceButton: Button ?= null
    private var backCallback: OnBackPressedCallback ?= null
    private val viewModel: ElectronicsViewModel by viewModels()

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
        viewModel.setAllDevices()
        viewModel.setCurrentDevice(arguments)
        viewModel.setBaseParameters()
        binding?.title?.setText(context?.getString(R.string.viewingProperties)+viewModel.device?.name)
        binding?.parameterContainer?.electronicsName?.setText(viewModel.device?.name)
        binding?.parameterContainer?.electronicsCategory?.setText(viewModel.device?.category)
        binding?.parameterContainer?.electronicsBrand?.setText(viewModel.device?.brand)
        binding?.parameterContainer?.electronicsWeight?.setText(viewModel.parameterToString(viewModel.weight)+context?.getString(R.string.kg))
        binding?.parameterContainer?.electronicsVoltage?.setText(viewModel.parameterToString(viewModel.voltage)+context?.getString(R.string.V))
        binding?.parameterContainer?.electronicsGuarantee?.setText(viewModel.device?.guarantee)
        binding?.parameterContainer?.electronicsPrice?.setText(viewModel.parameterToString(viewModel.price)+context?.getString(R.string.R))
        editDeviceButton = binding?.editBtn
        editDeviceButton?.setOnClickListener{
            val action = ViewPageDirections.actionViewFragmentToEditFragment()
            val device = viewModel.device
            action.electronicsParams = device?.toJSON() ?: viewModel.getDefaultDevice().toJSON()
            findNavController().navigate(action)
        }
    }

}