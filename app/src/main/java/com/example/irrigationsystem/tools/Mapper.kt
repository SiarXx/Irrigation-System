package com.example.irrigationsystem.tools

import kotlin.math.round

class Mapper () {
    fun humValueToPercent(value:Double):Double{
        var percent = value/9
        return round(percent)
    }
    fun humPercentToValue(value:Double):Double{
        return value*9
    }
}