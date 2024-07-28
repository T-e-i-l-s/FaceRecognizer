package ru.pyshop.recognizer.presentation.views

import android.graphics.Point
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import ru.pyshop.recognizer.domain.models.EffectsStatusModel
import ru.pyshop.recognizer.domain.models.FaceModel

// Функция зарисовки котура лица
@Composable
fun FaceContourView(
    contours: List<FaceModel>,
    effectsStatus: EffectsStatusModel,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // Проходим по всем обнаруженным лицам
        contours.forEach { contour ->
            if (effectsStatus.showFaceMask) { // Рисуем контур лица
                drawFaceContour(contour.contour)
            }
            if (effectsStatus.showGlasses) { // Рисуем очки
                drawGlasses(contour.leftEye, contour.rightEye)
            }
        }
    }
}

private fun DrawScope.drawFaceContour(contour: List<Point>) {
    val path = Path().apply {
        if (contour.isNotEmpty()) {
            // Начало контура
            val firstPoint = contour.first()
            moveTo(
                firstPoint.x.toFloat(),
                firstPoint.y.toFloat()
            )

            // Проходим по всем точкам
            contour.forEach { point ->
                // Рисуем круг
                drawCircle(
                    color = Color.White,
                    center = Offset(
                        point.x.toFloat(),
                        point.y.toFloat()
                    ),
                    radius = 3f,
                )
            }
        }
    }

    // Выводим контур
    drawPath(
        path = path,
        color = Color.White,
        style = Stroke(width = 1.dp.toPx())
    )
}

// Функция, которая рисует очки
private fun DrawScope.drawGlasses(leftEye: Point?, rightEye: Point?) {
    if (leftEye == null || rightEye == null) return

    val leftOffset = Offset(leftEye.x.toFloat(), leftEye.y.toFloat()) // Позиция левого глаза
    val rightOffset = Offset(rightEye.x.toFloat(), rightEye.y.toFloat()) // Позиция правого глаза

    // Вектор между центрами глаз
    val vector = rightOffset - leftOffset
    val distanceBetweenEyes = vector.getDistance()

    // Определяем размеры линз и толщину линий в зависимости от расстояния между глазами
    val glassesRadius = distanceBetweenEyes / 3 // Размер линз
    val strokeWidth = distanceBetweenEyes / 15 // Толщина линий

    val glassesColor = Color.Black // Цвет очков

    // Рисуем линзу на левом глазу
    drawCircle(
        color = glassesColor,
        center = leftOffset,
        radius = glassesRadius,
        style = Stroke(width = strokeWidth)
    )

    // Рисуем линзу на правом глазу
    drawCircle(
        color = glassesColor,
        center = rightOffset,
        radius = glassesRadius,
        style = Stroke(width = strokeWidth)
    )

    // Нормализуем вектор и умножаем на радиус линз
    val unitVector = vector / distanceBetweenEyes
    val adjustedVector = unitVector * glassesRadius

    // Новые точки начала и конца линии
    val lineStart = leftOffset + adjustedVector
    val lineEnd = rightOffset - adjustedVector

    // Соединяем линзы "очков"
    drawLine(
        color = glassesColor,
        start = lineStart,
        end = lineEnd,
        strokeWidth = strokeWidth
    )
}