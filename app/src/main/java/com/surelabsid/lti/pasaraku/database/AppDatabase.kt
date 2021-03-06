package com.surelabsid.lti.pasaraku.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notifications::class], version = 4, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun notificationDao(): NotificationsDao
}