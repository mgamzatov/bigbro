package com.magomed.gamzatov.carwatchprototype.ui.map

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.magomed.gamzatov.carwatchprototype.R
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback


class MapFragment: Fragment() {
    private val title = "Map"
    private var map: MapView? = null
    private var onMapReadyCallback: OnMapReadyCallback? = null

    companion object {
        /**
         * Creates a MapFragment instance

         * @param mapboxMapOptions The configuration options to be used.
         * *
         * @return MapFragment created.
         */
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    /**
     * Creates the fragment view hierarchy.

     * @param inflater           Inflater used to inflate content.
     * *
     * @param container          The parent layout for the map fragment.
     * *
     * @param savedInstanceState The saved instance state for the map fragment.
     * *
     * @return The view created
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val mapView = inflater.inflate(R.layout.fragment_map, container, false)
        map = mapView?.findViewById(R.id.mapView)
        val cameraListToolbar = mapView?.findViewById<Toolbar>(R.id.mapToolbar)
        cameraListToolbar?.title = title
        (activity as AppCompatActivity).setSupportActionBar(cameraListToolbar)

        return mapView
    }

    /**
     * Called when the fragment view hierarchy is created.

     * @param view               The content view of the fragment
     * *
     * @param savedInstanceState THe saved instance state of the framgnt
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map?.onCreate(savedInstanceState)
    }

    /**
     * Called when the fragment is visible for the users.
     */
    override fun onStart() {
        super.onStart()
        map?.onStart()
        map?.getMapAsync(onMapReadyCallback)
    }

    /**
     * Called when the fragment is ready to be interacted with.
     */
    override fun onResume() {
        super.onResume()
        map?.onResume()
    }

    /**
     * Called when the fragment is pausing.
     */
    override fun onPause() {
        super.onPause()
        map?.onPause()
    }

    /**
     * Called when the fragment state needs to be saved.

     * @param outState The saved state
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map?.onSaveInstanceState(outState)
    }

    /**
     * Called when the fragment is no longer visible for the user.
     */
    override fun onStop() {
        super.onStop()
        map?.onStop()
    }

    /**
     * Called when the fragment receives onLowMemory call from the hosting Activity.
     */
    override fun onLowMemory() {
        super.onLowMemory()
        map?.onLowMemory()
    }

    /**
     * Called when the fragment is view hiearchy is being destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        map?.onDestroy()
    }

    /**
     * Sets a callback object which will be triggered when the MapboxMap instance is ready to be used.

     * @param onMapReadyCallback The callback to be invoked.
     */
    fun getMapAsync(onMapReadyCallback: OnMapReadyCallback) {
        this.onMapReadyCallback = onMapReadyCallback
    }


}