package com.surelabsid.lti.pasaraku.utils

import java.text.SimpleDateFormat
import java.util.*


object HourToMillis {

    fun millis(): Long {
        val time = Calendar.getInstance(Locale.ENGLISH)
        return time.timeInMillis
    }

    fun addExpired(expiredOn: Int?, unit: Int = Calendar.HOUR): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())

        val date = sdf.parse(currentDateandTime)
        val calendar = Calendar.getInstance()
        calendar.time = date!!
        expiredOn?.let { calendar.add(unit, it) }
        return calendar.timeInMillis
    }

    private fun millisToDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val c = Calendar.getInstance()
        c.timeInMillis = millis
        return sdf.format(c.time)
    }

    fun millisToDateHour(millis: Long?): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy H:mm", Locale.getDefault())
        val c = Calendar.getInstance()
        c.timeInMillis = millis!!
        println("Time here " + sdf.format(c.time))
        return sdf.format(c.time)
    }

    fun millisToCustomFormat(millis: Long, format: String = "dd/MM/yyyy HH:mm"): String {
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        val c = Calendar.getInstance()
        c.timeInMillis = millis
        return sdf.format(c.time)
    }


}