package com.magomed.gamzatov.carwatchprototype.ui.custom


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.magomed.gamzatov.carwatchprototype.R


/**
 * A simple [Fragment] subclass.
 * Use the [CustomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomFragment : Fragment() {

    private val title: String = "Custom"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_custom, container, false)
        val customToolbar = view.findViewById<Toolbar>(R.id.customToolbar)
        customToolbar.title = title
        (activity as AppCompatActivity).setSupportActionBar(customToolbar)

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        fun newInstance(): CustomFragment {
            return CustomFragment()
        }
    }

}// Required empty public constructor
