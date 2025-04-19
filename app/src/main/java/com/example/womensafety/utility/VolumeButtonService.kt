package com.example.womensafety.utility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.example.womensafety.data.AppDatabase
import com.example.womensafety.ui.service.VolumeButtonViewModel
import com.example.womensafety.ui.service.VolumeButtonViewModelFactory

class VolumeButtonService : AccessibilityService() {

    private lateinit var viewModel: VolumeButtonViewModel
    private var buttonPressStartTime: Long = 0L
    private val holdThreshold = 3000L // 3 seconds for hold detection
    private var volumeUpPressCount: Int = 0
    private var lastVolumeUpPressTime: Long = 0L
    private val doublePressInterval = 2000L // 2 seconds for double-press

    override fun onServiceConnected() {
        super.onServiceConnected()
        val contactDao = AppDatabase.getDatabase(applicationContext).contactListDao()
        val policeHelplineDao = AppDatabase.getDatabase(applicationContext).policeHelplineDao()
        viewModel = VolumeButtonViewModelFactory(
            context = applicationContext,
            permissionUtils = PermissionUtils,
            contactDao = contactDao,
            policeHelplineDao = policeHelplineDao
        ).create(VolumeButtonViewModel::class.java)
        Log.d("VolumeButtonService", "Accessibility Service Connected")
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        when (event.keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> handleVolumeUp(event)
            KeyEvent.KEYCODE_VOLUME_DOWN -> handleVolumeDown(event)
        }
        return super.onKeyEvent(event)
    }

    private fun handleVolumeUp(event: KeyEvent) {
        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
                buttonPressStartTime = System.currentTimeMillis()
                Log.d("VolumeButtonService", "Volume Up Button Pressed")
            }
            KeyEvent.ACTION_UP -> {
                val pressDuration = System.currentTimeMillis() - buttonPressStartTime
                if (pressDuration < holdThreshold) {
                    Log.d("VolumeButtonService", "Volume Up Quick Press detected")
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastVolumeUpPressTime <= doublePressInterval) {
                        volumeUpPressCount++
                        if (volumeUpPressCount >= 2) {
                            Log.d("VolumeButtonService", "Volume Up Double Press detected")
                            viewModel.onVolumeUpDoublePress()
                            volumeUpPressCount = 0 // Reset count
                        }
                    } else {
                        volumeUpPressCount = 1 // First press
                    }
                    lastVolumeUpPressTime = currentTime
                }
            }
        }
    }

    private fun handleVolumeDown(event: KeyEvent) {
        when (event.action) {
            KeyEvent.ACTION_DOWN -> {
                buttonPressStartTime = System.currentTimeMillis()
                Log.d("VolumeButtonService", "Volume Down Button Pressed")
            }
            KeyEvent.ACTION_UP -> {
                val pressDuration = System.currentTimeMillis() - buttonPressStartTime
                if (pressDuration >= holdThreshold) {
                    Log.d("VolumeButtonService", "Volume Down Hold detected")
                    viewModel.onVolumeDownHold()
                } else {
                    Log.d("VolumeButtonService", "Volume Down Quick Press ignored")
                }
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not required for button actions
    }

    override fun onInterrupt() {
        Log.d("VolumeButtonService", "Accessibility Service Interrupted")
    }
}