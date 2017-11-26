package com.magomed.gamzatov.carwatchprototype.di

import com.magomed.gamzatov.carwatchprototype.ui.camera.CameraFragment
import com.magomed.gamzatov.carwatchprototype.ui.cameraList.CameraListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeCameraFragment(): CameraFragment

    @ContributesAndroidInjector
    abstract fun contributeCameraListFragment(): CameraListFragment
}