package com.whitenoisequran.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.whitenoisequran.data.local.entity.AmbientSoundEntity
import com.whitenoisequran.data.local.entity.ReciterEntity
import com.whitenoisequran.data.local.entity.SurahEntity
import com.whitenoisequran.domain.model.DownloadState
import kotlinx.coroutines.flow.Flow

@Dao
interface SurahDao {
    @Query("SELECT * FROM surahs WHERE reciterId = :reciterId ORDER BY number ASC")
    fun getSurahsByReciter(reciterId: Int): Flow<List<SurahEntity>>

    @Query("SELECT * FROM surahs WHERE number = :number AND reciterId = :reciterId LIMIT 1")
    suspend fun getSurah(number: Int, reciterId: Int): SurahEntity?

    @Query("SELECT COUNT(*) FROM surahs WHERE reciterId = :reciterId AND downloadState = 'DONE'")
    fun getCompletedCountFlow(reciterId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM surahs WHERE reciterId = :reciterId AND downloadState = 'FAILED'")
    fun getFailedCountFlow(reciterId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM surahs WHERE reciterId = :reciterId AND downloadState = 'DONE'")
    suspend fun getCompletedCount(reciterId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurahs(surahs: List<SurahEntity>)

    @Query("UPDATE surahs SET downloadState = :state, localFilePath = :localPath WHERE number = :number AND reciterId = :reciterId")
    suspend fun updateDownloadState(number: Int, reciterId: Int, state: DownloadState, localPath: String?)

    @Query("UPDATE surahs SET downloadState = 'NONE', localFilePath = NULL WHERE reciterId = :reciterId")
    suspend fun resetAllDownloadStates(reciterId: Int)

    @Query("SELECT COUNT(*) FROM surahs WHERE reciterId = :reciterId")
    suspend fun getSurahCount(reciterId: Int): Int
}

@Dao
interface ReciterDao {
    @Query("SELECT * FROM reciters ORDER BY id ASC")
    fun getAllReciters(): Flow<List<ReciterEntity>>

    @Query("SELECT * FROM reciters WHERE id = :id LIMIT 1")
    suspend fun getReciterById(id: Int): ReciterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReciters(reciters: List<ReciterEntity>)

    @Query("SELECT COUNT(*) FROM reciters")
    suspend fun getReciterCount(): Int
}

@Dao
interface AmbientSoundDao {
    @Query("SELECT * FROM ambient_sounds ORDER BY sortOrder ASC")
    fun getAllSounds(): Flow<List<AmbientSoundEntity>>

    @Query("SELECT * FROM ambient_sounds WHERE id = :id LIMIT 1")
    suspend fun getSoundById(id: String): AmbientSoundEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSounds(sounds: List<AmbientSoundEntity>)

    @Query("UPDATE ambient_sounds SET volume = :volume WHERE id = :id")
    suspend fun updateVolume(id: String, volume: Float)

    @Query("UPDATE ambient_sounds SET isEnabled = :isEnabled WHERE id = :id")
    suspend fun updateEnabled(id: String, isEnabled: Boolean)

    @Query("UPDATE ambient_sounds SET isEnabled = 0")
    suspend fun disableAll()

    @Query("SELECT COUNT(*) FROM ambient_sounds")
    suspend fun getSoundCount(): Int
}
