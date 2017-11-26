package com.magomed.gamzatov.carwatchprototype.ui.camera

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.github.niqdev.mjpeg.MjpegInputStream
import com.magomed.gamzatov.carwatchprototype.services.mjpeg.Mjpeg
import rx.Observable
import javax.inject.Inject


class CameraViewModel @Inject constructor(val mjpeg: Mjpeg): ViewModel() {

    var url = "http://93.92.207.123:555/REtex9Cn?container=mjpeg&stream=main"

    val observableCamera: LiveData<Observable<MjpegInputStream>> by lazy {
        mjpeg.getObservableCam(url)
    }

}