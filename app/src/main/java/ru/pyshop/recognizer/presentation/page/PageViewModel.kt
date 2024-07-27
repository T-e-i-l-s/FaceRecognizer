package ru.pyshop.recognizer.presentation.page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.pyshop.recognizer.domain.enums.CameraSide

// ViewModel основной страницы
class PageViewModel: ViewModel() {
    // Создаем mutableStateOf со значением стороны камеры, который является приватным
    private val _cameraSide = mutableStateOf(CameraSide.FRONT)
    // Делаем экземпляр _cameraSide с публичным доступом без возможности редактирования
    val cameraSide: State<CameraSide> get() = _cameraSide

    // Функция для переключения камер устройства
    fun switchCamera() {
        _cameraSide.value = if (_cameraSide.value == CameraSide.FRONT) CameraSide.BACK
                            else CameraSide.FRONT
    }
}