package com.magomed.gamzatov.carwatchprototype.ui.cameraList

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.magomed.gamzatov.carwatchprototype.R
import kotlinx.android.synthetic.main.camera_item.view.*


class CameraListAdapter(private val listener: (String) -> Unit) : RecyclerView.Adapter<CameraListAdapter.CameraViewHolder>(){

    var items: List<String> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CameraViewHolder(layoutInflater.inflate(R.layout.camera_item, parent, false))
    }

    override fun onBindViewHolder(holder: CameraViewHolder, position: Int) = holder.bind(items[position], listener)

    override fun getItemCount() = items.size

    class CameraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(str: String, listener: (String) -> Unit) = with(itemView) {
            cameraUrl.text = str
            setOnClickListener { listener(str) }
        }
    }
}