package com.example.womensafety.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.womensafety.model.ContactNumbers
import com.example.womensafety.model.Guardian
import com.example.womensafety.model.OTP
import com.example.womensafety.model.PoliceHelpline
import com.example.womensafety.model.User


@Database(entities = [ContactNumbers::class, Guardian::class, User::class, OTP::class, PoliceHelpline::class], version = 7, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactListDao(): ContactDao
    abstract fun guardianDao(): GuardianDao
    abstract fun userDao(): UserDao
    abstract fun otpDao(): OTPDao
    abstract fun policeHelplineDao(): PoliceHelplineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}