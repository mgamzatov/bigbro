package com.magomed.gamzatov.carwatchprototype.di

import com.magomed.gamzatov.carwatchprototype.services.camera.CameraListService
import com.magomed.gamzatov.carwatchprototype.services.mjpeg.Mjpeg
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {

    @Provides fun provideMjepg(): Mjpeg {
        return Mjpeg()
    }

    @Singleton @Provides fun provideCameraListService(): CameraListService {
        return CameraListService()
    }
}