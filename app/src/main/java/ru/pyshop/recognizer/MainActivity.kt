package ru.pyshop.recognizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
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