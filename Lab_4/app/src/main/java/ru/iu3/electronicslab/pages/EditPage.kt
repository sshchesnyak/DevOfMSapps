package ru.iu3.electronicslab.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.iu3.electronicslab.ElectronicsViewModel
import ru.iu3.electronicslab.R
import ru.iu3.electronicslab.Utils
import ru.iu3.electronicslab.databinding.EditPageBinding

class EditPage:Fragment(R.layout.edit_page) {

    private var binding: EditPageBinding ?= null
    private var saveDeviceButton: Button ?= null
    private var backCallback: OnBackPressedCallback ?= null
    private val viewModel: ElectronicsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backCallback = requireActivity().onBackPressedDispatcher.addCallback(this){
            saveContents()
            if (parentFragmentManager.backStackEntryCount == 1){
                val action = EditPageDirections.actionEditFragmentToMainFragment()
                findNavController().navigate(action)
            }
            if (parentFragmentManager.backStackEntryCount == 2){
                val action = EditPageDirections.actionEditFragmentToViewFragment()
                val device = viewModel.device
                action.electronicsParams = device?.toJSON() ?: viewModel.getDefaultDevice().toJSON()
                findNavController().navigate(action)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val thisBinding = EditPageBinding.bind(view)
        binding = thisBinding
        viewModel.setAllDevices()
        viewModel.setCurrentDevice(arguments)
        viewModel.setBaseParameters()
        binding?.title?.setText(context?.getString(R.string.editingProperties)+viewModel.device?.name)

        binding?.editContainer?.editName?.setText(viewModel.device?.name)
        binding?.editContainer?.editName?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding?.title?.setText(context?.getString(R.string.editingProperties)+p0)
                viewModel.device?.name = p0.toString()
                viewModel.edited = true
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        //Category spinner
        viewModel.setCategoryOptions()
        val categorySpinner: Spinner? = binding?.editContainer?.editCategory
        val categorySpinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context!!,android.R.layout.simple_spinner_item, viewModel.categoryOptions)
        categorySpinner?.adapter = categorySpinnerAdapter
        categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.getItemAtPosition(p2)==viewModel.getCategoryOption(0)) viewModel.categoryResponse=context?.getString(R.string.noCategory) ?:""
                else{
                    viewModel.device?.category = p0?.getItemAtPosition(p2).toString()
                    viewModel.edited = true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.device?.category = context?.getString(R.string.noCategory) ?:""
            }

        }
        binding?.editContainer?.editBrand?.setText(viewModel.device?.brand)
        binding?.editContainer?.editBrand?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.device?.brand=p0.toString()
                viewModel.edited=true
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding?.editContainer?.editWeight?.setText(viewModel.parameterToString(viewModel.weight))
        binding?.editContainer?.editWeight?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty()==true) viewModel.device?.weight=p0.toString().toDouble()
                else viewModel.device?.weight=0.0
                viewModel.edited=true
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding?.editContainer?.editVoltage?.setText(viewModel.parameterToString(viewModel.voltage))
        binding?.editContainer?.editVoltage?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty()==true) viewModel.device?.voltage=p0.toString().toDouble()
                else viewModel.device?.voltage=0.0
                viewModel.edited=true
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding?.editContainer?.editPrice?.setText(viewModel.parameterToString(viewModel.price))
        binding?.editContainer?.editPrice?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isNotEmpty()==true) viewModel.device?.price=(p0.toString()).toDouble()
                else viewModel.device?.price=0.0
                viewModel.edited=true
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding?.editContainer?.guaranteeContainer?.editGuarantee?.setText(viewModel.device?.getPeriod())
        binding?.editContainer?.guaranteeContainer?.editGuarantee?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.guarantee=p0.toString()
                viewModel.edited=true
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        //Guarantee spinner
        viewModel.setGuaranteeOptions()
        val guaranteeSpinner: Spinner? = binding?.editContainer?.guaranteeContainer?.guaranteeSpinner
        val guaranteeSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context!!,android.R.layout.simple_spinner_item, viewModel.guaranteeOptions)
        guaranteeSpinner?.adapter = guaranteeSpinnerAdapter
        guaranteeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.getItemAtPosition(p2)==viewModel.getGuaranteeOption(0)) viewModel.guaranteeResponse=context?.getString(R.string.NA) ?: ""
                else{
                    viewModel.guaranteeResponse = p0?.getItemAtPosition(p2).toString()
                    viewModel.edited = true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.guaranteeResponse = context?.getString(R.string.NA) ?: ""
            }

        }
        if (viewModel.device?.getType()!=""){
            guaranteeSpinner?.setSelection(guaranteeSpinnerAdapter.getPosition(viewModel.device?.getType()))
        }

        saveDeviceButton = binding?.saveBtn
        saveDeviceButton?.setOnClickListener{
            saveContents()
            if (parentFragmentManager.backStackEntryCount == 1){
                val action = EditPageDirections.actionEditFragmentToMainFragment()
                findNavController().navigate(action)
            }
            if (parentFragmentManager.backStackEntryCount == 2){
                val action = EditPageDirections.actionEditFragmentToViewFragment()
                val device = viewModel.device
                action.electronicsParams = device?.toJSON() ?: viewModel.getDefaultDevice().toJSON()
                findNavController().navigate(action)
            }
        }
    }

    private fun saveContents(){

        viewModel.device?.toGuarantee(viewModel.guarantee,viewModel.guaranteeResponse)
        if (viewModel.device?.guarantee!=viewModel.oldDevice?.guarantee) viewModel.edited = true
        if (viewModel.device?.name?.isEmpty() == true) viewModel.edited = true

        if (viewModel.edited){
            //actualizing data
            if (binding?.editContainer?.editName?.text?.isEmpty()==true) viewModel.device?.name = context?.getString(R.string.sampleName) ?: "Sample name"
            if (binding?.editContainer?.editName?.text?.isEmpty()==true) viewModel.device?.brand = context?.getString(R.string.sampleBrand) ?: "Sample brand"
            if (binding?.editContainer?.guaranteeContainer?.editGuarantee?.text?.isEmpty()==true) viewModel.device?.guarantee = "0 "+context?.getString(R.string.NA)
            if (binding?.editContainer?.editWeight?.text?.isEmpty()==true) viewModel.device?.weight=0.0
            if (binding?.editContainer?.editVoltage?.text?.isEmpty()==true) viewModel.device?.voltage=0.0
            if (binding?.editContainer?.editPrice?.text?.isEmpty()==true) viewModel.device?.price=0.0

            //if element is found within the list, then we have to remove it before
            //adding to the top of the list (editing an element)
            if (viewModel.allDevices.indexOf(viewModel.oldDevice)!=-1){
                viewModel.allDevices.removeAt(viewModel.allDevices.indexOf(viewModel.oldDevice))
            }
            //adding an element to the top of the list (creating new element)
            viewModel.allDevices.add(0, viewModel.device ?: viewModel.getDefaultDevice())
            Utils().writeToFile(viewModel.file,viewModel.allDevices)
        }
    }
}