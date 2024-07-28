package ru.pyshop.recognizer.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import ru.pyshop.recognizer.R

// Кастомный компонент круглой кнопки
@Composable
fun CircleButton(modifier: Modifier, painter: Painter, onClick: () -> Unit) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
            .height(60.dp)
            .width(60.dp)
            .background(colorResource(id = R.color.fill), CircleShape),
    ) {
        Icon(
            painter,
            tint = colorResource(id = R.color.content),
            contentDescription = null,
        )
    }
}