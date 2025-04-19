package com.example.womensafety.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.womensafety.data.ContactDao

class HomeViewModelFactory(
    private val context: Context,
    private val contactDao: ContactDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(context, contactDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}