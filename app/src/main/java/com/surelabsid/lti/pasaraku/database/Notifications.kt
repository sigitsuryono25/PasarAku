package com.surelabsid.lti.pasaraku.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.surelabsid.lti.pasaraku.utils.HourToMillis

@Entity
data class Notifications(
    @PrimaryKey
    val id: String = HourToMillis.millisToCustomFormat(HourToMillis.millis()),
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "timestamp")
    val timestamp: String?,
    @ColumnInfo(name = "message")
    val message: String?,
)