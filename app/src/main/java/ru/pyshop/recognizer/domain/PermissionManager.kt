package ru.pyshop.recognizer.domain

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

// Класс для запроса и контроля разрешений
class PermissionManager {
    // Получить разрешение, если оно еще не получено
    fun requestPermissionIfNeeded(context: Context) {
        if (!checkPermissions(context)) { // Если разрешение не получено ранее, то запрашиваем
            ActivityCompat.requestPermissions(
                context as Activity, cameraPermissions, 0
            )
        }
    }

    // Проверяем получены ли разрешения
    private fun checkPermissions(context: Context): Boolean {
        return cameraPermissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Список разрешений, необходимых для работы с камерой
    private val cameraPermissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO
    )
}