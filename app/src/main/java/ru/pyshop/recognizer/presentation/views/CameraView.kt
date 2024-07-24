package ru.pyshop.recognizer.presentation.views

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import ru.pyshop.recognizer.domain.enums.CameraSide

@Composable
fun CameraView(cameraSide: CameraSide) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(
                    if (cameraSide == CameraSide.FRONT) CameraSelector.LENS_FACING_FRONT
                    else CameraSelector.LENS_FACING_BACK
                )
                .build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                preview
            )
        }, ContextCompat.getMainExecutor(context))
    }

    AndroidView(
        { previewView },
        modifier = Modifier.fillMaxSize()
    )
}