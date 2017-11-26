package com.magomed.gamzatov.carwatchprototype.ui

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.View
import com.magomed.gamzatov.carwatchprototype.App
import com.magomed.gamzatov.carwatchprototype.R
import com.magomed.gamzatov.carwatchprototype.services.net.models.Camera
import com.magomed.gamzatov.carwatchprototype.services.net.models.Status
import com.magomed.gamzatov.carwatchprototype.services.net.models.UserMonitoring
import com.magomed.gamzatov.carwatchprototype.ui.camera.CameraFragment
import com.magomed.gamzatov.carwatchprototype.ui.cameraList.CameraListFragment
import com.magomed.gamzatov.carwatchprototype.ui.custom.CustomFragment
import com.magomed.gamzatov.carwatchprototype.ui.map.MapFragment
import com.magomed.gamzatov.carwatchprototype.util.setContentFragment
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.SupportMapFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.camera_item.*
import kotlinx.android.synthetic.main.fragment_map.*
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private val fragmentsMap: MutableMap<Int, Fragment?> = HashMap()

    private val mapTag = "com.mapbox.map"

    override fun getLifecycle() = lifecycleRegistry

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Mapbox.getInstance(this, getString(R.string.mapBoxApiKey))
        populateFragmentsList()
        mapFragment(savedInstanceState)
        navigation.setOnNavigationItemSelectedListener({
            selectFragment(it.itemId)
            true
        })
        selectFragment(R.id.navigation_map)
        val timer = Timer()

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    App.api.getMonitorings(1).enqueue(object : Callback<List<UserMonitoring>> {
                        override fun onResponse(call: Call<List<UserMonitoring>>, response: Response<List<UserMonitoring>>) {
                            val monitorings = response.body()
                            Log.d("getMonitorings", monitorings?.toString()?:"null")
                            monitorings?.forEach {
                                if(it.hereTheft)  {
                                    sendNotification(it.id.toInt())
                                    App.api.disableMonitoring(it.id).enqueue(object : Callback<Void> {
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                            Log.d("disableMonitoring", response.toString())
                                        }

                                        override fun onFailure(call: Call<Void>, t: Throwable) {
                                            Log.e("disableMonitoring error", t.message)
                                        }
                                    })
                                }
                            }
                        }
                        override fun onFailure(call: Call<List<UserMonitoring>>, t: Throwable) {
                            Log.e("getMonitorings error", t.message)
                        }
                    })
                    App.api.notifications().enqueue(object : Callback<Status> {
                        override fun onResponse(call: Call<Status>, response: Response<Status>) {
                            val status = response.body()
                            Log.d("notifications", status?.toString()?:"null")
                            if(status?.status != null) {
                                sendTextNotification(status.status)
                            }
                        }

                        override fun onFailure(call: Call<Status>, t: Throwable) {
                            Log.e("notifications error", t.message)
                        }
                    })
                }
            }
        }, 1000, 5000)
    }

    private fun populateFragmentsList() {
        fragmentsMap.put(R.id.navigation_camera, CameraFragment.newInstance())
    }

    private fun selectFragment(key: Int, tag: String? = null) {
        val fragment = fragmentsMap[key]
        val tagFragment = if(key == R.id.navigation_map) mapTag else tag ?: key.toString()
        if(fragment!=null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.containerLayout, fragment, tagFragment)
                    .commit()
        }
    }

    private fun mapFragment(savedInstanceState: Bundle?) {
        // Create supportMapFragment
        val mapFragment = if (savedInstanceState == null) {
            // Create map fragment
            MapFragment.newInstance()
        } else {
            supportFragmentManager?.findFragmentByTag(mapTag) as MapFragment?
        }
        fragmentsMap.put(R.id.navigation_map, mapFragment)
        mapFragment?.getMapAsync(OnMapReadyCallback { mapBox ->
            val markers = mutableMapOf<Long, Camera>()
            App.api.getCameras().enqueue(object : Callback<List<Camera>> {
                override fun onResponse(call: Call<List<Camera>>, response: Response<List<Camera>>) {
                    val cameras = response.body()
                    Log.d("getCameras", cameras?.toString())
                    cameras?.forEach {
                        val marker = mapBox.addMarker(MarkerViewOptions()
                                .position(LatLng(it.latitude, it.longitude))
                                .snippet(it.curCars.toString() + "/" + it.maxCars.toString())
//                                .snippet(it.url)
//                                .title(it.width.toString() + " " + it.height.toShort() + " " + it.id)
                        )
                        //marker.showInfoWindow(mapBox, mapView)
                        markers.put(marker.id, it)
                    }
                }
                override fun onFailure(call: Call<List<Camera>>, t: Throwable) {
                    Log.e("getCameras error", t.message)
                }
            })
            mapBox.markerViewManager.setOnMarkerViewClickListener { marker, _, _ ->
                if(marker.isInfoWindowShown) {
                    val camera = markers[marker.id]
                    CameraFragment.url = camera?.url
                    CameraFragment.imageWidth = camera?.width ?: -1
                    CameraFragment.imageHeight = camera?.height ?: -1
                    CameraFragment.cameraId = camera?.id ?: -1
                    navigation.selectedItemId = R.id.navigation_camera
                } else {
                    mapBox.selectMarker(marker)
                }
                true
            }
        })
    }

    private fun sendNotification(id:Int) {

        val mBuilder = NotificationCompat.Builder(this)

        //Create the intent that’ll fire when the user taps the notification//

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://10.100.11.214:8081/$id.gif"))
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.mipmap.ic_videocam_white)
        mBuilder.setContentTitle("SOS")
        mBuilder.setContentText("Your car in danger")
        mBuilder.priority = Notification.PRIORITY_MAX

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mNotificationManager.notify(id, mBuilder.build())
    }

    private fun sendTextNotification(text: String) {

        val mBuilder = NotificationCompat.Builder(this)

        //Create the intent that’ll fire when the user taps the notification//
        mBuilder.setSmallIcon(R.mipmap.ic_videocam_white)
        mBuilder.setContentTitle("Caution")
        mBuilder.setContentText(text)
        mBuilder.priority = Notification.PRIORITY_MAX

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }
}
