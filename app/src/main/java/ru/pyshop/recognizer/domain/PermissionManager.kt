package ru.pyshop.recognizer.domain

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionManager {
    fun requestPermissionIfNeeded(context: Context) {
        if (!checkPermissions(context)) {
            ActivityCompat.requestPermissions(
                context as Activity, cameraPermissions, 0
            )
        }
    }

    private fun checkPermissions(context: Context): Boolean {
        return cameraPermissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val cameraPermissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO
    )
}