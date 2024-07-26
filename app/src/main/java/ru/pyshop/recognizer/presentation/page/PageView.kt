package ru.pyshop.recognizer.presentation.page

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.pyshop.recognizer.domain.enums.CameraSide
import ru.pyshop.recognizer.presentation.views.cameraView.CameraView

// View основной страницы
@Composable
fun PageView() {
    // Объявляем ViewModel для этого View
    val viewModel: PageViewModel = viewModel()

    CameraView(CameraSide.BACK)
}