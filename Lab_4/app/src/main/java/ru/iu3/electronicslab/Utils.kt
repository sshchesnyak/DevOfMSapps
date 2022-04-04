package ru.iu3.electronicslab

import android.content.Context
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.File
import java.util.*
import kotlin.math.round

class Utils {

    private val fileName = "storage.txt"

    fun getDefaultDevice(context: Context?): ElectronicsItem {
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

    fun getFile(context: Context?): File {
        return File(context?.getExternalFilesDir(null),fileName)
    }

    fun getAllDevices(context: Context?): MutableList<ElectronicsItem>{
        val deviceList = mutableListOf<ElectronicsItem>()
        val file = getFile(context)
        val isNewFileCreated = file.createNewFile()
        if (!isNewFileCreated){
            val bufferedReader = file.bufferedReader()
            val jStrings = bufferedReader.readLines()
            for (jString in jStrings){
                val device = getDefaultDevice(context)
                device.fromJSON(jString)
                deviceList.add(device)
            }
            bufferedReader.close()
        }
        return deviceList
    }

    fun writeToFile(file: File, devices: MutableList<ElectronicsItem>){
        val bufferedWriter = file.bufferedWriter()
        for (i in 0 until devices.size) bufferedWriter.write(devices[i].toJSON() + "\n")
        bufferedWriter.close()
    }

    fun categoryListFormer(context: Context?):ArrayList<String>{
        val categoryOptions:ArrayList<String> = arrayListOf()
        categoryOptions.add(context?.getString(R.string.categorySelector)?:"")
        categoryOptions.add(context?.getString(R.string.categoryOne)?:"")
        categoryOptions.add(context?.getString(R.string.categoryTwo)?:"")
        categoryOptions.add(context?.getString(R.string.categoryThree)?:"")
        return categoryOptions
    }

    fun roundToTwoDecimals(item: Double?):Double{
        return round((item!!)*100/100)
    }

    fun guaranteeListFormer(context: Context?):ArrayList<String>{
        val guaranteeOptions:ArrayList<String> = arrayListOf()
        guaranteeOptions.add(context?.getString(R.string.guaranteeSelector)?:"")
        guaranteeOptions.add(context?.getString(R.string.NA)?:"")
        guaranteeOptions.add(context?.getString(R.string.months)?:"")
        guaranteeOptions.add(context?.getString(R.string.years)?:"")
        return guaranteeOptions
    }

    fun returnWeight(weight: Double?):String{
        return roundToTwoDecimals(weight).toString()
    }

    fun getWeightValue(weight: String):Double{
        return weight.toDouble()
    }

    fun returnVoltage(voltage: Double?):String{
        return roundToTwoDecimals(voltage).toString()
    }

    fun getVoltageValue(voltage: String):Double{
        return voltage.toDouble()
    }

    fun returnPrice(price: Double?):String{
        return roundToTwoDecimals(price).toString()
    }

    fun getPriceValue(price: String):Double{
        return price.toDouble()
    }

    object UUIDSerializer : KSerializer<UUID> {
        override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): UUID {
            return UUID.fromString(decoder.decodeString())
        }

        override fun serialize(encoder: Encoder, value: UUID) {
            encoder.encodeString(value.toString())
        }
    }

}