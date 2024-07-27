package ru.pyshop.recognizer.domain

import android.graphics.Point
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import ru.pyshop.recognizer.domain.enums.CameraSide

// Функция для обнаружения лиц
@OptIn(ExperimentalGetImage::class)
fun addFaceDetectionListener(
    imageProxy: ImageProxy,
    previewWidth: Int,
    previewHeight: Int,
    cameraSide: CameraSide,
    onPositionChange: (List<List<Point>>) -> Unit
) {
    val mediaImage = imageProxy.image // Получаем объект Image
    if (mediaImage != null) {
        // Переводим Image в InputImage
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        // Настраиваем модель разметки лиц
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

        // Получаем объект для обнаружения лиц
        val detector = FaceDetection.getClient(options)

        // Запускаем слушатель на обнаружение лица и завершение поиска лиц
        detector.process(image)
            .addOnSuccessListener { faces ->
                // Список всех точек всех лиц
                val allTransformedPoints: ArrayList<List<Point>> = ArrayList()

                // Проходим по лицам
                for (face in faces) {
                    // Переносим все точки на лице в List
                    val contourPoints = face.allContours
                        .flatMap { it.points }
                        .map { Point(it.x.toInt(), it.y.toInt()) }

                    // Расчитываем отношение сторон картинки, которую получаем с камеры
                    val imageAspectRatio = imageProxy.width.toFloat() / imageProxy.height
                    // Расчитываем отношение сторон картинки, которая видна на экране
                    val previewAspectRatio = previewWidth.toFloat() / previewHeight

                    // Переменные для верного отображения разметки
                    val scaleX: Float
                    val scaleY: Float
                    val offsetX: Float
                    val offsetY: Float

                    /*
                    Эти расчеты важны чтобы исправить смещение точек разметки
                    при отображении, без них разметка будет рядом, но не точно на лице.
                    */

                    if (imageAspectRatio > previewAspectRatio) {
                        // Вертикальное положение экрана и некоторые другие случаи
                        scaleY = previewHeight / imageProxy.width.toFloat()
                        scaleX = scaleY
                        offsetX = (previewWidth - (imageProxy.height * scaleY)) / 2f
                        offsetY = 0f
                    } else {
                        // Остальные случаи
                        scaleX = previewWidth / imageProxy.width.toFloat()
                        scaleY = scaleX
                        offsetX = 0f
                        offsetY = (previewHeight - (imageProxy.height * scaleY)) / 2f
                    }

                    // Применяем масштабирование и смещение
                    var transformedPoints = contourPoints.map { point ->
                        Point(
                            ((point.x * scaleX) + offsetX).toInt(),
                            ((point.y * scaleY) + offsetY).toInt()
                        )
                    }

                    // Если используется фронтальная камера, отражаем точки по горизонтали
                    if (cameraSide == CameraSide.FRONT) {
                        transformedPoints = transformedPoints.map { point ->
                            Point(previewWidth - point.x, point.y)
                        }
                    }

                    // Добавляем лицо в спаписок
                    allTransformedPoints.add(transformedPoints)
                }
                // Сообщаем об изменении
                onPositionChange(allTransformedPoints)
            }
            .addOnCompleteListener {
                // Прекращаем поиск лиц
                imageProxy.close()
            }
    } else {
        // Прекращаем поиск лиц
        imageProxy.close()
    }
}
