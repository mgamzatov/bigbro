package com.magomed.gamzatov.carwatchprototype.ui.cameraList


import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.magomed.gamzatov.carwatchprototype.R
import kotlinx.android.synthetic.main.fragment_camera_list.view.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import com.magomed.gamzatov.carwatchprototype.di.Injectable
import com.magomed.gamzatov.carwatchprototype.ui.MainActivity
import javax.inject.Inject

class CameraListFragment : Fragment(), Injectable {

    private val title = "List"
    private lateinit var cameraAdapter: CameraListAdapter
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera_list, container, false)
        val cameraListToolbar = view.findViewById<Toolbar>(R.id.cameraListToolbar)

        cameraListToolbar.title = title
        (activity as AppCompatActivity).setSupportActionBar(cameraListToolbar)

        cameraAdapter = CameraListAdapter(cameraItemOnClick)
        //view.cameraList.layoutManager = LinearLayoutManager(activity)
        //view.cameraList.adapter = cameraAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(CameraListViewModel::class.java)
        viewModel.observableCameraUrls.observe(this, Observer<List<String>> { cameraList ->
            if (cameraList != null) {
                cameraAdapter.items = cameraList
            }
        })
    }

    private val cameraItemOnClick = { item: String ->
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            Log.d("TAG clicked", item)
            //(activity as MainActivity).showCamera(item)
        }
    }

    companion object {
        fun newInstance() = CameraListFragment()
    }
}// Required empty public constructor
