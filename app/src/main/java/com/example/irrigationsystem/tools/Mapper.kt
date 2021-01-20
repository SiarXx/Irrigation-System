package com.example.irrigationsystem.tools

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
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
    fun getDateFromLongs(timeLong:Long,dateLong:Long):Date{
        // dmmyy/ddmmyy hmm/hhmm
        val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
        var dateString = ""
        val time = timeLong.toString()
        val date = dateLong.toString()
        dateString += if(time.length == 4) {
            time.substring(0, 2) + ":" + time.substring(2)
        }else{
            "0" + time.substring(0,1) + ":" + time.substring(1)
        }
        dateString += if(date.length == 6){
             " " + date.substring(0,2) + "/" + date.substring(2,4) + "/20" + date.substring(4)
        }else{
             " 0" + date.substring(0,1) + "/" + date.substring(1,3) + "/20" + date.substring(3)
        }
        val localDateTime = LocalDateTime.parse(dateString,dateTimeFormatter)
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun datePrintFormat(date:Double):String{
        val longDate = date.toLong()
        val dateToFormat = Date(longDate)
        val dtf = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy")
        val ldt = LocalDateTime.ofInstant(dateToFormat.toInstant(), ZoneId.systemDefault())
        return ldt.format(dtf)
    }
    fun dateLabelPrintFormat(date:Date):String{
        val dtf = DateTimeFormatter.ofPattern("HH:mm")
        val ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        return ldt.format(dtf)
    }
}