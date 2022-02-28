package ru.iu3.numlab

import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sqrt

data class NumberItem(val number: Int, var sign: String, var complexity: String) {
    fun setSign(){
        if (this.number<0) this.sign="negative"
        else{
            if (this.number>0) this.sign="positive"
            else this.sign="neutral"
        }
    }

    fun setComplexity(){
        val absval = abs(this.number.toDouble())
        if (absval>=2){
            for (j in 2..(1+round(sqrt(absval)).toInt())){
                if ((absval.toInt()%j == 0) and (absval.toInt()!=j)){
                    this.complexity = "composite"
                    break
                }
            }
            if (this.complexity!="composite") this.complexity="prime"
        }
        else this.complexity="neither"
    }

    fun createSubtitle(): String{
        return "This number is "+this.sign+" and "+this.complexity
    }

    override fun toString(): String {
        return this.number.toString()
    }
}