package ru.iu3.noteslab

import android.content.Context
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    private val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val fileName = "storage.txt"

    fun getDefaultNote(context: Context?): NoteItem {
        return NoteItem(
            UUID.randomUUID(),
            "",
            getCurrentDate(context),
            ""
        )
    }

    fun dateToString(date: Date): String{
        return formatter.format(date)
    }

    fun stringToDate(string: String): Date{
        return formatter.parse(string)!!
    }

    fun getCurrentDate(context: Context?): Date{
        return formatter.parse(formatter.format(Calendar.getInstance().time))
            ?: formatter.parse(context?.getString(R.string.defaultNoteDate))!!
    }

    fun getFile(context: Context?): File{
        //val file = File(context?.filesDir, fileName)
        return File(context?.getExternalFilesDir(null),fileName)
    }

    fun getAllNotes(context: Context?): MutableList<NoteItem>{
        val noteList = mutableListOf<NoteItem>()
        val file = getFile(context)
        val isNewFileCreated = file.createNewFile()
        if (!isNewFileCreated){
            val bufferedReader = file.bufferedReader()
            val jStrings = bufferedReader.readLines()
            for (jString in jStrings){
                val note = getDefaultNote(context)
                note.fromJSON(jString)
                noteList.add(note)
            }
            bufferedReader.close()
        }
        return noteList
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

    object DateSerializer: KSerializer<Date>{
        override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): Date {
            return Utils().stringToDate(decoder.decodeString())
        }

        override fun serialize(encoder: Encoder, value: Date) {
            encoder.encodeString(Utils().dateToString(value))
        }
    }

}