package ru.pyshop.recognizer.presentation.views.cameraView

import android.graphics.Point
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import ru.pyshop.recognizer.domain.addFaceDetectionListener
import ru.pyshop.recognizer.domain.enums.CameraSide
import ru.pyshop.recognizer.presentation.views.faceContourView.FaceContourView
import java.util.concurrent.Executors

// View, который демонстрирует изображение с камеры
@Composable
fun CameraView(cameraSide: CameraSide) {
    val context = LocalContext.current
    // Изображение с камеры, которое видит пользователь
    val previewView = remember { PreviewView(context) }
    // Список точек лица, если оно обнаружено
    val contour = remember { mutableStateOf<List<Point>?>(null) }

    // При запуске получаем изображение с камеры и начинаем искать лица
    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        // Создаем слушатель камеры
        cameraProviderFuture.addListener({
            // Получаем ProccessProviderFuture
            val cameraProvider = cameraProviderFuture.get()
            // Получаем картинку с камеры, которую будем выводить
            val preview = Preview.Builder().build()

            // Настраиваем камеру
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(
                    // Выбираем сторону камеры
                    if (cameraSide == CameraSide.FRONT) CameraSelector.LENS_FACING_FRONT
                    else CameraSelector.LENS_FACING_BACK
                )
                .build()

            // Начинаем обработку изоражения и поиск лиц
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                        // Подключаем ml kit
                        addFaceDetectionListener(imageProxy, previewView.width, previewView.height) { faceContour ->
                            // При обновлении данных о лицах локализируем их
                            contour.value = faceContour
                        }
                    }
                }

            // Даем Preview знать что мы готовы к получению данных
            preview.setSurfaceProvider(previewView.surfaceProvider)

            // Соединяем жизненный цикл камеры с приложением
            cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
        }, ContextCompat.getMainExecutor(context))
    }

    // Изображение с камеры
    AndroidView(
        { previewView },
        modifier = Modifier.fillMaxSize()
    )

    // Отображаем разметку лица, которую получаем из ml kit
    contour.value?.let {
        FaceContourView(contour = it, modifier = Modifier.fillMaxSize())
    }
}
