package ru.pyshop.recognizer.domain.models

/* Модель данных, которая хранит какие эффекты применять к изображению
   (Отображать ли контур лица и маску) */
data class EffectsStatusModel(
    val showFaceMask: Boolean,
    val showGlasses: Boolean,
)
