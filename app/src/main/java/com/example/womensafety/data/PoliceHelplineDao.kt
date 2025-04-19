package com.example.womensafety.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.womensafety.model.PoliceHelpline

@Dao
interface PoliceHelplineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contacts: List<PoliceHelpline>)

    @Query("SELECT * FROM police_helpline WHERE city = :city LIMIT 1")
    suspend fun getContactByCity(city: String): PoliceHelpline?

    @Query("SELECT * FROM police_helpline")
    suspend fun getAllContacts(): List<PoliceHelpline>

    @Query("SELECT COUNT(*) FROM police_helpline")
    suspend fun getHelplineCount(): Int
}
