package com.magomed.gamzatov.carwatchprototype

import android.app.Activity
import android.app.Application
import com.magomed.gamzatov.carwatchprototype.di.AppInjector
import com.magomed.gamzatov.carwatchprototype.services.net.Api
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application(), HasActivityInjector {
    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    private lateinit var retrofit: Retrofit

    companion object {
        lateinit var api: Api
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        retrofit = Retrofit.Builder()
                .baseUrl("http://10.100.11.214:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        api = retrofit.create(Api::class.java)
    }
}