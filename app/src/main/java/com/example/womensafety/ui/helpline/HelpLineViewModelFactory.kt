package com.example.womensafety.ui.helpline

import com.example.womensafety.data.PoliceHelplineDao
import com.example.womensafety.utility.PermissionUtils

class HelpLineViewModelFactory(
    private val context: android.content.Context,
    private val permissionUtils: PermissionUtils,
    private val policeHelplineDao: PoliceHelplineDao
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        // TODO: Implement factory to create HelpLineViewModel with PoliceHelplineDao
        @Suppress("UNCHECKED_CAST")
        return HelpLineViewModel() as T
    }
}