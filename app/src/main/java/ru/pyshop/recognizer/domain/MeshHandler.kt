package ru.pyshop.recognizer.domain

import android.graphics.PointF
import com.google.mlkit.vision.facemesh.FaceMesh

/* Функция для получения средней позиции из списка индексов.
   Нужна чтобы находить центр глаза по сетке лица и отображать линзы очков на нем. */
fun getAveragePosition(face: FaceMesh, indices: List<Int>): PointF? {
    val positions = indices.mapNotNull { face.allPoints.getOrNull(it)?.position }
    if (positions.isEmpty()) return null

    val avgX = positions.map { it.x }.average().toFloat()
    val avgY = positions.map { it.y }.average().toFloat()

    return PointF(avgX, avgY)
}
