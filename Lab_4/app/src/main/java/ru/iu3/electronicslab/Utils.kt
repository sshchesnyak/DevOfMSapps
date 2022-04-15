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

    fun getDefaultDevice(): ElectronicsItem {
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
                val device = getDefaultDevice()
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

    fun roundToTwoDecimals(item: Double?):Double{
        return round((item!!)*100/100)
    }

    fun returnPrice(price: Double?):String{
        return roundToTwoDecimals(price).toString()
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