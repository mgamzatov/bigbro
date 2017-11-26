package com.magomed.gamzatov.carwatchprototype.ui.cameraList

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.magomed.gamzatov.carwatchprototype.services.camera.CameraListService
import javax.inject.Inject


class CameraListViewModel @Inject constructor(cameraListService: CameraListService): ViewModel() {

    val observableCameraUrls: LiveData<List<String>> = cameraListService.getAsLiveData()

}