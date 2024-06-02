package com.example.yawndetection.ui.screens.camera

import androidx.camera.core.CameraSelector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class CameraViewModel: ViewModel() {
    val lensFacing = MutableStateFlow(CameraSelector.LENS_FACING_BACK)
    fun changeFacing() {
        lensFacing.value =

         if (lensFacing.value == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
    }
}