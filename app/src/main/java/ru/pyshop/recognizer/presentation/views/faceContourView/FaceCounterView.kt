package ru.pyshop.recognizer.presentation.views.faceContourView

import android.graphics.Point
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun FaceContourView(contour: List<Point>, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawFaceContour(contour)
    }
}

private fun DrawScope.drawFaceContour(contour: List<Point>) {
    val path = Path().apply {
        if (contour.isNotEmpty()) {
            val firstPoint = contour.first()
            moveTo(
                firstPoint.x.toFloat(),
                firstPoint.y.toFloat()
            )

            contour.forEach { point ->
                drawRect(
                    color = Color.White,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        point.x.toFloat() - 4.5f,
                        point.y.toFloat() - 4.5f
                    ),
                    size = androidx.compose.ui.geometry.Size(9f, 9f)
                )
            }
        }
    }

    drawPath(
        path = path,
        color = Color.White,
        style = Stroke(width = 1.dp.toPx())
    )
}
