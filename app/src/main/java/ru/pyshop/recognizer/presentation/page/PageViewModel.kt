package ru.pyshop.recognizer.presentation.page

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.pyshop.recognizer.domain.enums.CameraSide

// ViewModel основной страницы
class PageViewModel : ViewModel() {
    /* Создаем mutableStateOf с логическим значением включено ли
    отображение контура лица, который является приватным */
    private val _showFaceMask = mutableStateOf(true)

    // Делаем экземпляр _showFaceMask с публичным доступом без возможности редактирования
    val showFaceMask: State<Boolean> get() = _showFaceMask

    // Функция для включения/выключения отображения контура лица
    fun switchFaceMaskState() {
        _showFaceMask.value = !_showFaceMask.value
    }


    /* Создаем mutableStateOf с логическим значением включено ли
    отображение "очков", который является приватным */
    private val _showGlasses = mutableStateOf(true)

    // Делаем экземпляр _showGlasses с публичным доступом без возможности редактирования
    val showGlasses: State<Boolean> get() = _showGlasses

    // Функция для включения/выключения отображения "очков"
    fun switchGlassesState() {
        _showGlasses.value = !_showGlasses.value
    }


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