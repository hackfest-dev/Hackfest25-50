package com.example.womensafety.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.womensafety.model.Guardian

@Dao
interface GuardianDao {
    @Query("SELECT * FROM guardian LIMIT 1")
    suspend fun getGuardian(): Guardian?

    @Insert
    suspend fun insertGuardian(guardian: Guardian)

    @Update
    suspend fun updateGuardian(guardian: Guardian)

    @Query("DELETE FROM guardian")
    suspend fun clearGuardian()
}

