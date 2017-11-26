package com.magomed.gamzatov.carwatchprototype.ui.camera


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.niqdev.mjpeg.DisplayMode
import com.github.niqdev.mjpeg.Mjpeg

import com.magomed.gamzatov.carwatchprototype.R
import com.magomed.gamzatov.carwatchprototype.di.Injectable
import kotlinx.android.synthetic.main.fragment_camera.*
import android.view.MotionEvent
import android.widget.ImageView
import com.magomed.gamzatov.carwatchprototype.App
import com.magomed.gamzatov.carwatchprototype.services.net.models.Car
import com.magomed.gamzatov.carwatchprototype.services.net.models.Id
import com.magomed.gamzatov.carwatchprototype.services.net.models.Monitoring
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.graphics.BlurMaskFilter
import android.graphics.Shader.TileMode
import android.graphics.RadialGradient
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.mapbox.mapboxsdk.Mapbox.getApplicationContext
import org.jetbrains.anko.support.v4.toast


/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment(), Injectable {

    private val title = "Camera"
//    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
//    private lateinit var cameraViewModel: CameraViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (arguments != null) {
//            url = arguments?.getString(CAMERA_URL_PARAM)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        val cameraToolbar = view.findViewById<Toolbar>(R.id.cameraToolbar)
        cameraToolbar.title = title
        (activity as AppCompatActivity).setSupportActionBar(cameraToolbar)

        // Inflate the layout for this fragment
        return view
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        cameraViewModel = ViewModelProviders.of(this, viewModelFactory).get(CameraViewModel::class.java)
//        cameraViewModel.url = cameraUrl ?: cameraViewModel.url
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view = drawAllBlur()
        getCars(view)
    }

    fun drawAllBlur(): View? {
        val width = 1080
        val height = 1710
        val bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bg)
        val paintBlur = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBlur.color = Color.BLACK
        paintBlur.maskFilter = BlurMaskFilter(180F, BlurMaskFilter.Blur.INNER)

        if(activity != null) {
            canvas.drawRect(Rect(0, 500, width, height-500), paintBlur)
            val iV = ImageView(activity)
            iV.setImageBitmap(bg)
            iV.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            cameraContainer.addView(iV)

            return iV
        }
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    fun drawCars(cars: List<Car>?, view: View?) {
        val width = 1080
        val height = 1710

        val multiplier: Float = width.toFloat() / imageWidth

        val imageWidth = imageWidth * multiplier
        val imageHeight = imageHeight * multiplier

        val bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bg)
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.isAntiAlias = true

        val paintBlur = Paint(Paint.ANTI_ALIAS_FLAG)
        paintBlur.color = Color.BLACK
        paintBlur.maskFilter = BlurMaskFilter(80F, BlurMaskFilter.Blur.INNER)

        val left = (width /2) - (imageWidth/2)
        val top = height/2 - (imageHeight/2)
        val right = width/2 - (imageWidth/2) + imageWidth
        val bottom = height/2 - (imageHeight/2) + imageHeight

        val cameraRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        val carRects = mutableMapOf<Rect, Car>()
        canvas.drawRect(cameraRect, paint)
        cars?.forEach {
            val carRect = Rect((left + it.left * multiplier).toInt(), (top + it.top * multiplier).toInt(),
                    (left + it.right * multiplier).toInt(), (top + it.bottom * multiplier).toInt())
            carRects.put(carRect, it)
            canvas.drawRect(carRect, paintBlur)
            canvas.drawRect(carRect, paint)
        }

        if(activity!=null) {
            val iV = ImageView(activity)
            iV.setImageBitmap(bg)
            iV.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            iV.setOnTouchListener { _, event ->
                val touchX = event.x
                val touchY = event.y

                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (cameraRect.contains(touchX.toInt(), touchY.toInt())) {
                        carRects.forEach {
                            if (it.key.contains(touchX.toInt(), touchY.toInt())) {
                                onCarSelected(touchX, touchY, it.value, iV, view)
                                return@forEach
                            }
                        }
                    }
                }

                true
            }
            view?.visibility = View.INVISIBLE
            cameraContainer.addView(iV)
        }
    }

    private fun onCarSelected(touchX: Float, touchY: Float, car: Car, view: View, allView: View?) {
        Log.d("onTouch", touchX.toString() + " " + touchY.toString())
        Log.d("onTouch", car.toString())
        App.api.postMonitoring(Monitoring(Id(1),
                car.left, car.right, car.top, car.bottom, Id(cameraId)))
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        toast("monitoring started")
                        view.visibility = View.INVISIBLE
                        allView?.visibility = View.VISIBLE
                        Log.d("postMonitoring", response.toString())
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("postMonitoring error", t.message)
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        if(url != null) {
            Mjpeg.newInstance().open(url, 5)?.subscribe({
                mjpegView.setSource(it)
                mjpegView.setDisplayMode(DisplayMode.BEST_FIT)
                mjpegView.showFps(true)
            })
        }
    }

    private fun getCars(view: View?) {
        App.api.getCars(cameraId).enqueue(object : Callback<List<Car>> {
            override fun onResponse(call: Call<List<Car>>, response: Response<List<Car>>) {
                val cars = response.body()
                Log.d("getCars by " + cameraId, cars?.toString()?:"null")
                drawCars(cars, view)
            }
            override fun onFailure(call: Call<List<Car>>, t: Throwable) {
                Log.e("getCameras error", t.message)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        mjpegView.stopPlayback()

    }

    companion object {
        private val CAMERA_URL_PARAM = "camera_url_param"

        var url: String? = null
        var imageWidth: Int = -1
        var imageHeight: Int = -1
        var cameraId: Long = -1

        fun newInstance(): CameraFragment {
            val fragment = CameraFragment()
//            val args = Bundle()
//            args.putString(CAMERA_URL_PARAM, url)
//            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
