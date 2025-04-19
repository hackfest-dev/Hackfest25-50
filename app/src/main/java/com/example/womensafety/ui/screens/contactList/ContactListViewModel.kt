package com.example.womensafety.ui.screens.contactList

import android.R.attr.phoneNumber
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.womensafety.data.ContactDao
import com.example.womensafety.model.ContactNumbers
import com.example.womensafety.utility.PermissionUtils

import kotlinx.coroutines.launch

class ContactListViewModel(
    private val context: Context,
    private val permissionUtils: PermissionUtils,
    private val contactDao: ContactDao
) : ViewModel() {

    val contactList = mutableStateListOf<ContactNumbers>()
    private var onContactPicked: ((Intent) -> Unit)? = null

    init {
        loadContactsFromDatabase()
    }

    private fun loadContactsFromDatabase() {
        viewModelScope.launch {
            contactList.clear()
            contactList.addAll(contactDao.getAllContacts())
        }
    }

    fun onAddContactClicked(onPickContact: (Intent) -> Unit) {
        if (permissionUtils.isPermissionGranted(android.Manifest.permission.READ_CONTACTS)) {
            onContactPicked = onPickContact
            onPickContact(Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI))
        } else {
            permissionUtils.requestPermission(android.Manifest.permission.READ_CONTACTS)
        }
    }

    fun handleContactPicked(data: Intent?) {
        data?.data?.let { uri ->
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val phoneIndex = it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val name = it.getString(nameIndex)
                    val phone = it.getString(phoneIndex).replace("\\s".toRegex(), "")
                    val contact = ContactNumbers(name = name, phoneNumber = phone)
                    saveContact(contact)
                }
            }
        }
    }

    private fun saveContact(contact: ContactNumbers) {
        viewModelScope.launch {
            contactDao.insertContact(contact)
            contactList.add(contact)
            Log.d("ContactListViewModel", "Inserted contact: $contact")
        }
    }

    fun deleteContact(contact: ContactNumbers) {
        viewModelScope.launch {
            contactDao.deleteContact(contact)
            contactList.remove(contact)
        }
    }

    fun updateContact(oldContact: ContactNumbers, newName: String) {
        viewModelScope.launch {
            val updatedContact = oldContact.copy(name = newName)
            contactDao.updateContact(updatedContact)
            contactList.remove(oldContact)
            contactList.add(updatedContact)
        }
    }

    fun onBackClicked(popUpScreen: () -> Unit) {
        popUpScreen()
    }
}

class ContactListViewModelFactory(
    private val context: Context,
    private val permissionUtils: PermissionUtils,
    private val contactDao: ContactDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactListViewModel(context, permissionUtils, contactDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}