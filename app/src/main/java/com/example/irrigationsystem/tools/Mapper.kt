package com.example.irrigationsystem.tools

import kotlin.math.round

class Mapper () {
    fun humValueToPercent(value:Double):Double{
        val percent = value/9
        return round(percent)
    }
    fun humPercentToValue(percent:Double):Double{
        val value = percent*9
        return round(value)
    }
}