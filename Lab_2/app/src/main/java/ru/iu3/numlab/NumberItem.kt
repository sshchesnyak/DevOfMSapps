package ru.iu3.numlab

import android.content.Context
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.sqrt

data class NumberItem(val number: Int, var sign: String, var complexity: String, var context: Context) {

    fun setSign(){
        if (this.number<0) this.sign=context.getString(R.string.negative_sub)
        else{
            if (this.number>0) this.sign=context.getString(R.string.positive_sub)
            else this.sign=context.getString(R.string.neutral_sub)
        }
    }

    fun setComplexity(){
        val absval = abs(this.number.toDouble())
        if (absval>=2){
            for (j in 2..(1+round(sqrt(absval)).toInt())){
                if ((absval.toInt()%j == 0) and (absval.toInt()!=j)){
                    this.complexity = context.getString(R.string.composite_sub)
                    break
                }
            }
            if (this.complexity!=context.getString(R.string.composite_sub)) this.complexity=context.getString(R.string.prime_sub)
        }
        else this.complexity=context.getString(R.string.neither_sub)
    }

    fun createSubtitle(): String{
        return context.getString(R.string.numDetails)+this.sign+context.getString(R.string.and)+this.complexity
    }

    override fun toString(): String {
        return this.number.toString()
    }
}