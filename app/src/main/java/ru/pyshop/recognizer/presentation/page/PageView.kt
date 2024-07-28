package ru.pyshop.recognizer.presentation.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.pyshop.recognizer.R
import ru.pyshop.recognizer.domain.models.EffectsStatusModel
import ru.pyshop.recognizer.presentation.views.CameraView
import ru.pyshop.recognizer.presentation.views.CircleButton

// View основной страницы
@Composable
fun PageView() {
    // Объявляем ViewModel для этого View
    val viewModel: PageViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()) {
        CameraView(
            viewModel.cameraSide.value,
            EffectsStatusModel(viewModel.showFaceMask.value, viewModel.showGlasses.value),
            Modifier.fillMaxSize()
        )

        Column(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            CircleButton(
                modifier = Modifier,
                painter = if (viewModel.showFaceMask.value) painterResource(id = R.drawable.face_mask_on_icon)
                else painterResource(id = R.drawable.face_mask_off_icon)
            ) {
                viewModel.switchFaceMaskState()
            }

            Spacer(modifier = Modifier.height(10.dp))

            CircleButton(
                modifier = Modifier,
                painter = if (viewModel.showGlasses.value) painterResource(id = R.drawable.glasses_on_icon)
                else painterResource(id = R.drawable.glasses_off_icon)
            ) {
                viewModel.switchGlassesState()
            }

            Spacer(modifier = Modifier.height(10.dp))

            CircleButton(
                modifier = Modifier,
                painter = painterResource(id = R.drawable.switch_camera_icon)
            ) {
                viewModel.switchCamera()
            }
        }
    }
}