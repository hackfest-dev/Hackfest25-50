package com.example.womensafety.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.womensafety.model.OTP

@Dao
interface OTPDao {
    @Query("SELECT * FROM otp WHERE phoneNumber = :phoneNumber")
    suspend fun getOTP(phoneNumber: String): OTP?

    @Insert
    suspend fun insertOTP(otp: OTP)

    @Query("DELETE FROM otp WHERE phoneNumber = :phoneNumber")
    suspend fun deleteOTP(phoneNumber: String)
}