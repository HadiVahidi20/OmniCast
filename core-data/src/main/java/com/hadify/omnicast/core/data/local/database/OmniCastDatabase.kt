package com.hadify.omnicast.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hadify.omnicast.core.data.local.dao.UserDao
import com.hadify.omnicast.core.data.local.dao.ZodiacDao
import com.hadify.omnicast.core.data.local.entity.UserEntity
import com.hadify.omnicast.core.data.local.entity.ZodiacEntity

@Database(
    entities = [
        UserEntity::class,
        ZodiacEntity::class
    ],
    version = 1,
    exportSchema = false
)
// انوتیشن @TypeConverters(Converters::class) از اینجا حذف شد تا تداخل با Hilt برطرف شود
abstract class OmniCastDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun zodiacDao(): ZodiacDao

    companion object {
        const val DATABASE_NAME = "omnicast_db"
    }
}