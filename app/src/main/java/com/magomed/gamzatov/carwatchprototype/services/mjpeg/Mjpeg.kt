package com.magomed.gamzatov.carwatchprototype.services.mjpeg

import android.arch.lifecycle.MutableLiveData
import com.github.niqdev.mjpeg.Mjpeg
import com.github.niqdev.mjpeg.MjpegInputStream
import rx.Observable

class Mjpeg {
    private val DEFAULT_TIMEOUT = 5 //seconds

    fun getObservableCam(camUrl: String, timeout: Int = DEFAULT_TIMEOUT): MutableLiveData<Observable<MjpegInputStream>> {
        val data = MutableLiveData<Observable<MjpegInputStream>>()
        data.postValue(Mjpeg.newInstance().open(camUrl, timeout))
        return data
    }

}