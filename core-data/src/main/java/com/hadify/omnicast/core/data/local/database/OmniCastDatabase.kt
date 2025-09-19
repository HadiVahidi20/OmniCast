package com.hadify.omnicast.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hadify.omnicast.core.data.local.converter.Converters
import com.hadify.omnicast.core.data.local.dao.PlaceholderDao
import com.hadify.omnicast.core.data.local.dao.UserDao
import com.hadify.omnicast.core.data.local.dao.ZodiacDao
import com.hadify.omnicast.core.data.local.entity.PlaceholderEntity
import com.hadify.omnicast.core.data.local.entity.UserEntity
import com.hadify.omnicast.core.data.local.entity.WeeklyZodiacEntity
import com.hadify.omnicast.core.data.local.entity.ZodiacEntity

@Database(
    entities = [
        PlaceholderEntity::class,
        UserEntity::class,
        ZodiacEntity::class,
        WeeklyZodiacEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OmniCastDatabase : RoomDatabase() {
    abstract fun placeholderDao(): PlaceholderDao
    abstract fun userDao(): UserDao
    abstract fun zodiacDao(): ZodiacDao
}