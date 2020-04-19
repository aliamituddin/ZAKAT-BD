package com.cyclicsoft.zakatbd

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cyclicsoft.zakatbd.home.model.ServiceBuilder
import com.cyclicsoft.zakatbd.home.model.TimingData
import com.cyclicsoft.zakatbd.home.model.TimingDataEndPoint
import com.google.gson.Gson
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.cyclicsoft.zakatbd.PreferenceHelper.get
import com.cyclicsoft.zakatbd.PreferenceHelper.set
import com.cyclicsoft.zakatbd.home.model.Timings


class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
    private var timingData: TimingData? = null
    private lateinit var prefs: SharedPreferences
    override fun onStart() {
        prefs = PreferenceHelper.defaultPrefs(applicationContext)
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupBlurView()
        setupViws()
        callTimingApi()
    }

    private fun callTimingApi() {
        val request = ServiceBuilder.buildService(TimingDataEndPoint::class.java)
        val call = request.getTimings(22.5931, 89.3168, 2)

        call.enqueue(object : Callback<TimingData> {
            override fun onResponse(call: Call<TimingData>, response: Response<TimingData>) {
                if (response.isSuccessful) {
                    timingData = response.body()
                    saveData(Gson().toJson(timingData?.data?.timings))
                }
            }

            override fun onFailure(call: Call<TimingData>, t: Throwable) {
                Log.d("Call", "${t.message}")
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveData(timingJson: String?) {
        //set any type of value in prefs
        prefs[AppConstants.KEY_PRAYER_TIMING] = timingJson
        //get any value from prefs
        val prayerTimings: String? = prefs[AppConstants.KEY_PRAYER_TIMING]
        updatePrayerTimingUi(prayerTimings)
        Log.d(TAG, "$prayerTimings")
    }

    private fun updatePrayerTimingUi(prayerTimings: String?) {
        val timings = Gson().fromJson(prayerTimings, Timings::class.java)
        tv_fajr_time?.text = timings?.fajr
        tv_duhr_time?.text = timings?.dhuhr
        tv_asr_time?.text = timings?.asr
        tv_magrib_time?.text = timings?.maghrib
        tv_isha_time?.text = timings?.isha


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

    private fun setupViws() {
        img_toolbar_left?.setImageResource(R.drawable.ic_help_outline_black_24dp)
        img_toolbar_left?.visibility = View.VISIBLE

        img_toolbar_right?.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        img_toolbar_right?.visibility = View.VISIBLE
    }
}
