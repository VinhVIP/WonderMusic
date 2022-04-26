package com.team28.wondermusic.data.database.daos

import androidx.room.*
import com.team28.wondermusic.data.database.entities.NotificationEntity

@Dao
interface NotificationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListNotification(notifications: List<NotificationEntity>)

    @Query("SELECT * FROM NotificationEntity ORDER BY idNotification DESC")
    suspend fun getListNotifications(): List<NotificationEntity>

    @Query("DELETE FROM NotificationEntity")
    suspend fun deleteListNotifications()

    @Delete
    suspend fun deleteNotification(notification: NotificationEntity)

    @Update
    suspend fun updateNotification(notification: NotificationEntity)

    @Query("UPDATE NotificationEntity SET notificationStatus = 1")
    suspend fun readAllNotification()
}