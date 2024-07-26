package ru.pyshop.recognizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.pyshop.recognizer.domain.PermissionManager
import ru.pyshop.recognizer.presentation.page.PageView

class MainActivity : ComponentActivity() {
    private val permissionManager = PermissionManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Проверяем разрешения и, при необходимости, запрашиваем их
        permissionManager.requestPermissionIfNeeded(this)

        setContent {
            PageView()
        }
    }
}