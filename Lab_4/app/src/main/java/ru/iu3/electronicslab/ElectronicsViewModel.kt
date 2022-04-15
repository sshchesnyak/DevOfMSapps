package ru.iu3.electronicslab

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

class ElectronicsViewModel(application:Application):AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context:Context = getApplication<Application>().applicationContext
    private val fileName = "storage.txt"
    val file: File = File(context.getExternalFilesDir(null),fileName)

    var device:ElectronicsItem ?= null
    var oldDevice:ElectronicsItem ?= null
    val allDevices: MutableList<ElectronicsItem> = mutableListOf()

    var weight:Double? = 0.0
    var voltage:Double? = 0.0
    var price:Double? = 0.0
    var guarantee:String = ""
    var edited:Boolean = false

    val categoryOptions: ArrayList<String> = arrayListOf()
    val guaranteeOptions: ArrayList<String> = arrayListOf()
    var categoryResponse: String = ""
    var guaranteeResponse: String = ""


    @ExperimentalSerializationApi
    fun setAllDevices(){
        val isNewFileCreated = file.createNewFile()
        if (!isNewFileCreated){
            val bufferedReader = file.bufferedReader()
            val jStrings = bufferedReader.readLines()
            for (jString in jStrings){
                val device = getDefaultDevice()
                device.fromJSON(jString)
                allDevices.add(device)
            }
            bufferedReader.close()
        }
    }

    @ExperimentalSerializationApi
    fun setCurrentDevice(arguments: Bundle?){
        device = getDefaultDevice().fromJSON(arguments?.getString("electronicsParams").toString())
        oldDevice = getDefaultDevice().fromJSON(arguments?.getString("electronicsParams").toString())
    }

    fun getDefaultDevice():ElectronicsItem{
        return ElectronicsItem(
            UUID.randomUUID(),
            "",
            "",
            "",
            0.0,
            0.0,
            "",
            0.0
        )
    }

    fun setCategoryOptions(){
        categoryOptions.add(context.getString(R.string.categorySelector))
        categoryOptions.add(context.getString(R.string.categoryOne))
        categoryOptions.add(context.getString(R.string.categoryTwo))
        categoryOptions.add(context.getString(R.string.categoryThree))
    }

    fun getCategoryOption(index: Int):String{
        return categoryOptions[index]
    }

    fun setBaseParameters(){
        guarantee = device?.getPeriod().toString()
        weight = device?.weight
        voltage = device?.voltage
        price = device?.price
    }

    fun parameterToString(parameter: Double?):String{
        return round((parameter!!)*100/100).toString()
    }

    fun setGuaranteeOptions(){
        guaranteeOptions.add(context.getString(R.string.guaranteeSelector))
        guaranteeOptions.add(context.getString(R.string.NA))
        guaranteeOptions.add(context.getString(R.string.months))
        guaranteeOptions.add(context.getString(R.string.years))
    }

    fun getGuaranteeOption(index: Int):String{
        return guaranteeOptions[index]
    }
}