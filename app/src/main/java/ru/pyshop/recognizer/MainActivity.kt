package ru.pyshop.recognizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.pyshop.recognizer.domain.PermissionManager
import ru.pyshop.recognizer.presentation.pages.mainPage.MainPageView

class MainActivity : ComponentActivity() {
    private val permissionManager = PermissionManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionManager.requestPermissionIfNeeded(this)

        setContent {
            MainPageView()
        }
    }
}