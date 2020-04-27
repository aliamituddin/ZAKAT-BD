package com.cyclicsoft.zakatbd

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cyclicsoft.zakatbd.home.model.HomeViewModel
import com.cyclicsoft.zakatbd.home.model.Timings
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : AppCompatActivity() {
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setUpToolBar()
        bindViews()
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        observeViewModelData()
        initListeners()
        bindData()
        //Do not change calling order
    }

    private fun bindViews() {
        setupBlurView()
    }

    private fun observeViewModelData() {
        homeViewModel.prayerTimings.observe(this, Observer {
            updatePrayerTimingUi(it)
        })

        homeViewModel.errMsg.observe(this, Observer {
            showError(it)
        })
    }

    private fun initListeners() {
    }

    private fun bindData() {
        Log.d(TAG + AppConstants.TAG_TIMING_API, "initUpdateData()")
        homeViewModel.loadTimingData()

//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                Log.d(TAG + AppConstants.TAG_TIMING_API,
//                    "fusedLocationClient.lastLocation.addOnSuccessListener >>>Location $location"
//                )
//                homeViewModel.requestTimingData(location, DateTime().year, Errors.UNKNOWN, Errors.UNKNOWN, AppConstants.REQUEST_CODE_LOCATION)
//            }
////            homeViewModel.requestTimingData(null, DateTime().year, "Paikgacha", "Bangladesh", AppConstants.REQUEST_CODE_ADDRESS)

    }

    private fun updatePrayerTimingUi(prayerTimings: Timings) {
        Log.d(
            TAG + ":" + AppConstants.TAG_TIMING_API,
            "updatePrayerTimingUi(\n" +
                    "prayerTimings: $prayerTimings\n" +
                    ")"
        )
        tv_fajr_time?.text = prayerTimings.fajr
        tv_duhr_time?.text = prayerTimings.dhuhr
        tv_asr_time?.text = prayerTimings.asr
        tv_magrib_time?.text = prayerTimings.maghrib
        tv_isha_time?.text = prayerTimings.isha
    }


    private fun showError(msg: String?) {
        //TODO...make a nice ui
        if (!msg.isNullOrBlank())
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    private fun setupBlurView() {
        val radius = 12f
        val minBlurRadius = 10f
        val step = 4f
        //set background, if your root layout doesn't have one
        val windowBackground = window.decorView.background
        blur_view.setupWith(root)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setHasFixedTransformationMatrix(true)
    }

    private fun setUpToolBar() {
        img_toolbar_left?.setImageResource(R.drawable.ic_help_outline_black_24dp)
        img_toolbar_left?.visibility = View.VISIBLE

        img_toolbar_right?.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        img_toolbar_right?.visibility = View.VISIBLE
    }
}
