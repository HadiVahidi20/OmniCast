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

    // Daily Horoscope Operations
    @Query("SELECT * FROM horoscope_readings WHERE zodiacSignName = :zodiacSignName AND date = :date LIMIT 1")
    suspend fun getHoroscopeForDate(zodiacSignName: String, date: LocalDate): ZodiacEntity?

    @Query("SELECT * FROM horoscope_readings WHERE id = :id")
    suspend fun getHoroscopeById(id: String): ZodiacEntity?

    @Query("SELECT * FROM horoscope_readings WHERE zodiacSignName = :zodiacSignName ORDER BY date DESC LIMIT :limit")
    fun getRecentHoroscopes(zodiacSignName: String, limit: Int = 30): Flow<List<ZodiacEntity>>

    @Query("SELECT * FROM horoscope_readings WHERE zodiacSignName = :zodiacSignName ORDER BY date DESC")
    fun getAllHoroscopes(zodiacSignName: String): Flow<List<ZodiacEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoroscope(horoscope: ZodiacEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoroscopes(horoscopes: List<ZodiacEntity>): List<Long>

    @Query("DELETE FROM horoscope_readings WHERE date < :cutoffDate")
    suspend fun deleteOldHoroscopes(cutoffDate: LocalDate): Int

    @Query("SELECT COUNT(*) > 0 FROM horoscope_readings WHERE zodiacSignName = :zodiacSignName AND date = :date")
    suspend fun hasHoroscopeForDate(zodiacSignName: String, date: LocalDate): Boolean

    @Query("DELETE FROM horoscope_readings WHERE zodiacSignName = :zodiacSignName")
    suspend fun deleteHoroscopesForSign(zodiacSignName: String): Int

    @Query("DELETE FROM horoscope_readings")
    suspend fun clearAllHoroscopes(): Int

    // Weekly Horoscope Operations
    @Query("SELECT * FROM weekly_horoscope_readings WHERE zodiacSignName = :zodiacSignName AND weekStartDate = :startDate LIMIT 1")
    suspend fun getWeeklyHoroscope(zodiacSignName: String, startDate: LocalDate): WeeklyZodiacEntity?

    @Query("SELECT * FROM weekly_horoscope_readings WHERE id = :id")
    suspend fun getWeeklyHoroscopeById(id: String): WeeklyZodiacEntity?

    @Query("SELECT * FROM weekly_horoscope_readings WHERE zodiacSignName = :zodiacSignName ORDER BY weekStartDate DESC LIMIT :limit")
    fun getRecentWeeklyHoroscopes(zodiacSignName: String, limit: Int = 10): Flow<List<WeeklyZodiacEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyHoroscope(weeklyHoroscope: WeeklyZodiacEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeeklyHoroscopes(weeklyHoroscopes: List<WeeklyZodiacEntity>): List<Long>

    @Query("DELETE FROM weekly_horoscope_readings WHERE weekStartDate < :cutoffDate")
    suspend fun deleteOldWeeklyHoroscopes(cutoffDate: LocalDate): Int

    @Query("SELECT COUNT(*) > 0 FROM weekly_horoscope_readings WHERE zodiacSignName = :zodiacSignName AND weekStartDate = :startDate")
    suspend fun hasWeeklyHoroscopeForWeek(zodiacSignName: String, startDate: LocalDate): Boolean

    @Query("DELETE FROM weekly_horoscope_readings WHERE zodiacSignName = :zodiacSignName")
    suspend fun deleteWeeklyHoroscopesForSign(zodiacSignName: String): Int

    @Query("DELETE FROM weekly_horoscope_readings")
    suspend fun clearAllWeeklyHoroscopes(): Int

    // Utility Queries
    @Query("SELECT DISTINCT zodiacSignName FROM horoscope_readings ORDER BY zodiacSignName")
    suspend fun getAllStoredZodiacSigns(): List<String>

    @Query("SELECT COUNT(*) FROM horoscope_readings")
    suspend fun getHoroscopeCount(): Int

    @Query("SELECT COUNT(*) FROM weekly_horoscope_readings")
    suspend fun getWeeklyHoroscopeCount(): Int
}