package ru.pyshop.recognizer.presentation.pages.mainPage

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.pyshop.recognizer.domain.enums.CameraSide
import ru.pyshop.recognizer.presentation.views.CameraView

@Composable
fun MainPageView() {
    val viewModel: MainPageViewModel = viewModel()

    CameraView(CameraSide.BACK)
}