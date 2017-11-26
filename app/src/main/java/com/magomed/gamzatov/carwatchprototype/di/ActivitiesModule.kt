package com.magomed.gamzatov.carwatchprototype.di

import com.magomed.gamzatov.carwatchprototype.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivitiesModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = arrayOf(FragmentBuildersModule::class))
    abstract fun contributeMainActivityInjector(): MainActivity
}