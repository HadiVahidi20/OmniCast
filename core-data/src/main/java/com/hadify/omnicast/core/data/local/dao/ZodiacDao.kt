package com.hadify.omnicast.core.data.local.dao

import androidx.room.*
import com.hadify.omnicast.core.data.local.entity.ZodiacEntity
import com.hadify.omnicast.core.data.local.entity.WeeklyZodiacEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * DAO for zodiac-related database operations
 * Located in core-data to avoid circular dependencies
 */
@Dao
interface ZodiacDao {

    @Query("SELECT * FROM horoscope_readings WHERE zodiacSignName = :zodiacSignName AND date = :date LIMIT 1")
    suspend fun getHoroscopeForDate(zodiacSignName: String, date: LocalDate): ZodiacEntity?

    @Query("SELECT * FROM horoscope_readings WHERE id = :id")
    suspend fun getHoroscopeById(id: String): ZodiacEntity?

    @Query("SELECT * FROM horoscope_readings WHERE zodiacSignName = :zodiacSignName ORDER BY date DESC")
    fun getCachedHoroscopes(zodiacSignName: String): Flow<List<ZodiacEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoroscope(horoscope: ZodiacEntity): Long

    @Query("DELETE FROM horoscope_readings WHERE date < :cutoffDate")
    suspend fun deleteOldHoroscopes(cutoffDate: LocalDate): Int

    @Query("SELECT COUNT(*) > 0 FROM horoscope_readings WHERE zodiacSignName = :zodiacSignName AND date = :date")
    suspend fun hasHoroscopeForDate(zodiacSignName: String, date: LocalDate): Boolean

    // Weekly horoscope operations
    @Query("SELECT * FROM weekly_horoscope_readings WHERE zodiacSignName = :zodiacSignName AND weekStartDate = :startDate LIMIT 1")
    suspend fun getWeeklyHoroscope(zodiacSignName: String, startDate: LocalDate): WeeklyZodiacEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyHoroscope(weeklyHoroscope: WeeklyZodiacEntity): Long

    @Query("DELETE FROM horoscope_readings")
    suspend fun clearAllHoroscopes()
}