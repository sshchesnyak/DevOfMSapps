package ru.iu3.electronicslab.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.iu3.electronicslab.ElectronicsItem
import ru.iu3.electronicslab.R
import ru.iu3.electronicslab.Utils
import ru.iu3.electronicslab.databinding.EditPageBinding

class EditPage:Fragment(R.layout.edit_page) {

    private var binding: EditPageBinding ?= null
    private var saveDeviceButton: Button ?= null
    private var currentDevice: ElectronicsItem?= null
    private var backCallback: OnBackPressedCallback ?= null

    private var edited: Boolean = false
    private var categoryResponse: String = ""
    private var weight: String = ""
    private var voltage: String = ""
    private var guaranteeResponse: String = ""
    private var price: String = ""

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
                val device = currentDevice
                action.electronicsParams = device?.toJSON() ?: Utils().getDefaultDevice(context).toJSON()
                findNavController().navigate(action)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val thisBinding = EditPageBinding.bind(view)
        binding = thisBinding
        currentDevice = Utils().getDefaultDevice(context).fromJSON(arguments?.getString("electronicsParams").toString())
        binding?.title?.setText(context?.getString(R.string.editingProperties)+currentDevice?.name)

        binding?.editContainer?.editName?.setText(currentDevice?.name)
        binding?.editContainer?.editName?.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding?.title?.setText(context?.getString(R.string.editingProperties)+p0)
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        //Category spinner
        val categoryOptions: ArrayList<String> = Utils().categoryListFormer(context)
        val categorySpinner: Spinner? = binding?.editContainer?.editCategory
        val categorySpinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context!!,android.R.layout.simple_spinner_item, categoryOptions)
        categorySpinner?.adapter = categorySpinnerAdapter
        categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.getItemAtPosition(p2)==categoryOptions[0]) categoryResponse=context?.getString(R.string.noCategory) ?:""
                else{
                    categoryResponse = p0?.getItemAtPosition(p2).toString()
                    edited = true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                categoryResponse = context?.getString(R.string.noCategory) ?:""
            }

        }
        binding?.editContainer?.editBrand?.setText(currentDevice?.brand)
        weight=Utils().returnWeight(currentDevice?.weight)
        binding?.editContainer?.editWeight?.setText(weight)
        voltage=Utils().returnVoltage(currentDevice?.voltage)
        binding?.editContainer?.editVoltage?.setText(voltage)
        binding?.editContainer?.guaranteeContainer?.editGuarantee?.setText(currentDevice?.getPeriod())
        //Guarantee spinner
        val guaranteeOptions: ArrayList<String> = Utils().guaranteeListFormer(context)
        val guaranteeSpinner: Spinner? = binding?.editContainer?.guaranteeContainer?.guaranteeSpinner
        val guaranteeSpinnerAdapter: ArrayAdapter<String> = ArrayAdapter(context!!,android.R.layout.simple_spinner_item, guaranteeOptions)
        guaranteeSpinner?.adapter = guaranteeSpinnerAdapter
        guaranteeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0?.getItemAtPosition(p2)==guaranteeOptions[0]) guaranteeResponse=context?.getString(R.string.NA) ?: ""
                else{
                    guaranteeResponse = p0?.getItemAtPosition(p2).toString()
                    edited = true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                categoryResponse = context?.getString(R.string.NA) ?: ""
            }

        }
        if (currentDevice?.getType()!=""){
            guaranteeSpinner?.setSelection(guaranteeSpinnerAdapter.getPosition(currentDevice?.getType()))
        }
        price=Utils().returnPrice(currentDevice?.price)
        binding?.editContainer?.editPrice?.setText(price)

        saveDeviceButton = binding?.saveBtn
        saveDeviceButton?.setOnClickListener{
            saveContents()
            if (parentFragmentManager.backStackEntryCount == 1){
                val action = EditPageDirections.actionEditFragmentToMainFragment()
                findNavController().navigate(action)
            }
            if (parentFragmentManager.backStackEntryCount == 2){
                val action = EditPageDirections.actionEditFragmentToViewFragment()
                val device = currentDevice
                action.electronicsParams = device?.toJSON() ?: Utils().getDefaultDevice(context).toJSON()
                findNavController().navigate(action)
            }
        }
    }

    fun saveContents(){
        //check if content was edited
        if (currentDevice?.name!=binding?.editContainer?.editName?.text.toString()) edited = true
        if (currentDevice?.brand!=binding?.editContainer?.editBrand?.text.toString()) edited = true
        val modWeight = Utils().returnWeight(Utils().getWeightValue(binding?.editContainer?.editWeight?.text.toString()))
        if (weight!=modWeight) edited = true
        val modVoltage = Utils().returnVoltage(Utils().getVoltageValue(binding?.editContainer?.editVoltage?.text.toString()))
        if (voltage!=modVoltage) edited = true
        val guarantee:String = currentDevice?.toGuarantee(binding?.editContainer?.guaranteeContainer?.editGuarantee?.text.toString(),guaranteeResponse) ?: ""
        if (currentDevice?.guarantee!=guarantee) edited = true
        val modPrice = Utils().returnPrice(Utils().getPriceValue(binding?.editContainer?.editPrice?.text.toString()))
        if(price!=modPrice) edited = true
        if(currentDevice?.name=="") edited = true

        if (edited){
            //initializing variables
            val devices: MutableList<ElectronicsItem> = Utils().getAllDevices(context)
            val index = devices.indexOf(currentDevice)
            val file = Utils().getFile(context)

            //actualizing data
            if (binding?.editContainer?.editName?.text.toString()=="") currentDevice?.name = context?.getString(R.string.sampleName) ?: "Sample name"
            else currentDevice?.name = binding?.editContainer?.editName?.text.toString()
            currentDevice?.category = categoryResponse
            if (binding?.editContainer?.editBrand?.text.toString()=="") currentDevice?.brand = context?.getString(R.string.sampleBrand) ?: "Sample brand"
            else currentDevice?.brand = binding?.editContainer?.editBrand?.text.toString()
            currentDevice?.weight = Utils().getWeightValue(binding?.editContainer?.editWeight?.text.toString())
            currentDevice?.voltage = Utils().getVoltageValue(binding?.editContainer?.editVoltage?.text.toString())
            currentDevice?.guarantee = guarantee
            currentDevice?.price = Utils().getPriceValue(binding?.editContainer?.editPrice?.text.toString())

            //if element is found within the list, then we have to remove it before
            //adding to the top of the list (editing an element)
            if (index!=-1) devices.removeAt(index)
            //adding an element to the top of the list (creating new element)
            devices.add(0, currentDevice ?: Utils().getDefaultDevice(context))
            Utils().writeToFile(file,devices)
        }

        //clearing space before quitting
        binding?.editContainer?.editName?.setText("")
        binding?.editContainer?.editBrand?.setText("")
        binding?.editContainer?.editWeight?.setText("")
        binding?.editContainer?.editVoltage?.setText("")
        binding?.editContainer?.guaranteeContainer?.editGuarantee?.setText("")
        binding?.editContainer?.editPrice?.setText("")
    }
}