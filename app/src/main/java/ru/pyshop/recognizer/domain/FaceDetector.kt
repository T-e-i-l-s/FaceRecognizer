package ru.pyshop.recognizer.domain

import android.graphics.Point
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.facemesh.FaceMeshDetection
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions
import ru.pyshop.recognizer.domain.enums.CameraSide
import ru.pyshop.recognizer.domain.models.FaceModel

// Функция для обнаружения лиц
@OptIn(ExperimentalGetImage::class)
fun addFaceDetectionListener(
    imageProxy: ImageProxy,
    previewWidth: Int,
    previewHeight: Int,
    cameraSide: CameraSide,
    onPositionChange: (List<FaceModel>) -> Unit
) {
    val mediaImage = imageProxy.image // Получаем объект Image
    if (mediaImage != null) {
        // Переводим Image в InputImage
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        // Настраиваем модель разметки лиц
        val options = FaceMeshDetectorOptions.Builder()
            .setUseCase(FaceMeshDetectorOptions.FACE_MESH)
            .build()

        // Получаем объект для обнаружения сетки лица
        val detector = FaceMeshDetection.getClient(options)

        // Запускаем слушатель на обнаружение лица и завершение поиска лиц
        detector.process(image)
            .addOnSuccessListener { faces ->
                // Список всех точек всех лиц
                val allTransformedPoints: ArrayList<FaceModel> = ArrayList()

                // Проходим по лицам
                for (face in faces) {
                    // Переносим все точки на лице в List
                    val meshPoints = face.allPoints
                        .map { Point(it.position.x.toInt(), it.position.y.toInt()) }

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
                    var transformedPoints = meshPoints.map { point ->
                        Point(
                            ((point.x * scaleX) + offsetX).toInt(),
                            ((point.y * scaleY) + offsetY).toInt()
                        )
                    }

                    // Индексы точек вокруг левого и правого глаза
                    val leftEyeIndices = listOf(133, 155, 154, 145, 144, 153, 163, 144, 160, 161)
                    val rightEyeIndices = listOf(362, 382, 381, 374, 373, 380, 390, 373, 387, 388)
                    // Получаем положение глаз для отображения очков
                    val leftEyePosition = getAveragePosition(face, leftEyeIndices)
                    val rightEyePosition = getAveragePosition(face, rightEyeIndices)

                    // Высчитываем смещение глаз(выше написано зачем)
                    var transformedLeftEye: Point? = null
                    var transformedRightEye: Point? = null

                    if (leftEyePosition != null) { // Если глаз обнаружен
                        // Высчитываем смещение левого глаза
                        transformedLeftEye = Point(
                            ((leftEyePosition.x * scaleX) + offsetX).toInt(),
                            ((leftEyePosition.y * scaleY) + offsetY).toInt()
                        )
                    }

                    if (rightEyePosition != null) { // Если глаз обнаружен
                        // Высчитываем смещение правого глаза
                        transformedRightEye = Point(
                            ((rightEyePosition.x * scaleX) + offsetX).toInt(),
                            ((rightEyePosition.y * scaleY) + offsetY).toInt()
                        )
                    }

                    // Если используется фронтальная камера, отражаем все точки по горизонтали
                    if (cameraSide == CameraSide.FRONT) {
                        transformedPoints = transformedPoints.map { point ->
                            Point(previewWidth - point.x, point.y)
                        }
                        transformedLeftEye?.let {
                            transformedLeftEye = Point(previewWidth - it.x, it.y)
                        }
                        transformedRightEye?.let {
                            transformedRightEye = Point(previewWidth - it.x, it.y)
                        }
                    }

                    // Добавляем точки лица в список
                    allTransformedPoints.add(
                        FaceModel(
                            transformedPoints,
                            transformedLeftEye,
                            transformedRightEye
                        )
                    )
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

