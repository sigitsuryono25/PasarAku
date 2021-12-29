package com.surelabsid.lti.pasaraku.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query

@Dao
interface NotificationsDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAll(): List<Notifications>

    @Insert(onConflict = IGNORE)
    fun insertAll(vararg notifications: Notifications)

    @Delete
    fun delete(notifications: Notifications)

    @Query("DELETE FROM notifications")
    fun nukeTable()
}