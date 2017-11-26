package com.magomed.gamzatov.carwatchprototype.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.magomed.gamzatov.carwatchprototype.ui.camera.CameraViewModel
import com.magomed.gamzatov.carwatchprototype.ui.cameraList.CameraListViewModel
import com.magomed.gamzatov.carwatchprototype.viewmodel.AppViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(value = CameraListViewModel::class)
    abstract fun bindCameraListViewModel(cameraListViewModel: CameraListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = CameraViewModel::class)
    abstract fun bindCameraViewModel(cameraViewModel: CameraViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}