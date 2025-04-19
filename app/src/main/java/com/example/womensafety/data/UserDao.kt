package com.example.womensafety.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.womensafety.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE phoneNumber = :phoneNumber")
    suspend fun getUser(phoneNumber: String): User?

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM user")
    suspend fun clearUsers()

    @Query("SELECT * FROM user WHERE guardianPhoneNumber = :guardianPhone LIMIT 1")
    suspend fun getChildByGuardian(guardianPhone: String): User?

    @Query("SELECT * FROM user WHERE phoneNumber = (SELECT guardianPhoneNumber FROM user WHERE phoneNumber = :childPhone LIMIT 1) LIMIT 1")
    suspend fun getGuardianByChild(childPhone: String): User?
}