package com.example.womensafety.ui.service

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.womensafety.data.ContactDao
import com.example.womensafety.data.PoliceHelplineDao
import com.example.womensafety.utility.PermissionUtils

class VolumeButtonViewModelFactory(
    private val context: Context,
    private val permissionUtils: PermissionUtils,
    private val contactDao: ContactDao,
    private val policeHelplineDao: PoliceHelplineDao
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VolumeButtonViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VolumeButtonViewModel(context, permissionUtils, contactDao, policeHelplineDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}