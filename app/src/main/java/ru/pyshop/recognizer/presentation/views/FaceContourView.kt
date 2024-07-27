package ru.pyshop.recognizer.presentation.views

import android.graphics.Point
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

// Функция зарисовки котура лица
@Composable
fun FaceContourView(contours: List<List<Point>>, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // Проходим по всем обнаруженным лицам
        contours.forEach { contour ->
            drawFaceContour(contour)
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
                    center = androidx.compose.ui.geometry.Offset(
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