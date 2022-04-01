package ru.iu3.noteslab

import com.google.gson.annotations.Expose
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

@Serializable
data class NoteItem(
    @Expose @Serializable(with=Utils.UUIDSerializer::class) var id: UUID,
    @Expose var title: String,
    @Expose @Serializable(with=Utils.DateSerializer::class) var date: Date,
    @Expose var content: String){


    fun toJSON() : String {
        return Json.encodeToString(serializer(),this)
    }

    @ExperimentalSerializationApi
    fun fromJSON(jString: String) : NoteItem{
        val item = Json.decodeFromString<NoteItem>(jString)
        this.id = item.id
        this.title = item.title
        this.date = item.date
        this.content = item.content
        return this
    }

}
