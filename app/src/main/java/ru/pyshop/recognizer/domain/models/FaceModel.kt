package ru.pyshop.recognizer.domain.models

import android.graphics.Point

/* Модель данных, которая хранит элементы, которые нужны
   для корректного отображения различных эффектов */
data class FaceModel(
    val contour: List<Point>,
    val leftEye: Point?,
    val rightEye: Point?,
)
