package com.magomed.gamzatov.carwatchprototype.services.camera

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData


class CameraListService {

    private val camerasUrlList = arrayListOf(
            "http://93.92.207.123:555/REtex9Cn?container=mjpeg&stream=main"
    )

    fun getAsLiveData():LiveData<List<String>> {
        val data = MutableLiveData<List<String>>()
        data.postValue(camerasUrlList)
        return data
    }
}