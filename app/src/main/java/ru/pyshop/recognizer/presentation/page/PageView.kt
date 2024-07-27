package ru.pyshop.recognizer.presentation.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.pyshop.recognizer.presentation.views.CameraView
import ru.pyshop.recognizer.presentation.views.SwitchCameraButton

// View основной страницы
@Composable
fun PageView() {
    // Объявляем ViewModel для этого View
    val viewModel: PageViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()) {
        CameraView(viewModel.cameraSide.value, Modifier.fillMaxSize())
        SwitchCameraButton(modifier = Modifier.align(Alignment.BottomEnd)) {
            viewModel.switchCamera()
        }
    }
}