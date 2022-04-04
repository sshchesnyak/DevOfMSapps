package ru.iu3.electronicslab

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

@Serializable
data class ElectronicsItem(@Serializable(with=Utils.UUIDSerializer::class)var id: UUID,
                           var name: String,
                           var category: String,
                           var brand: String,
                           var weight: Double,
                           var voltage: Double,
                           var guarantee: String,
                           var price: Double) {

    fun toJSON() : String {
        return Json.encodeToString(serializer(),this)
    }

    @ExperimentalSerializationApi
    fun fromJSON(jString: String) : ElectronicsItem{
        val item = Json.decodeFromString<ElectronicsItem>(jString)
        this.id = item.id
        this.name = item.name
        this.category = item.category
        this.brand = item.brand
        this.weight = item.weight
        this.voltage = item.voltage
        this.guarantee = item.guarantee
        this.price = item.price
        return this
    }

    fun toGuarantee(period: String, type: String): String{
        this.guarantee = "$period $type"
        return this.guarantee
    }

    fun getPeriod():String{
        var value = ""
        if (this.guarantee.isNotEmpty()) value = this.guarantee.substring(0,this.guarantee.indexOf(" "))
        return value
    }

    fun getType():String{
        var value = ""
        if (this.guarantee.isNotEmpty()) value = this.guarantee.substring(this.guarantee.indexOf(" "))
        return value
    }

}