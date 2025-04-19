package com.example.womensafety.data

import androidx.room.*
import com.example.womensafety.model.ContactNumbers

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<ContactNumbers>

    @Insert
    suspend fun insertContact(contact: ContactNumbers)

    @Update
    suspend fun updateContact(contact: ContactNumbers)

    @Delete
    suspend fun deleteContact(contact: ContactNumbers)
}