package com.cyclicsoft.zakatbd.home.model

import android.app.Application
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyclicsoft.zakatbd.AppConstants
import com.cyclicsoft.zakatbd.Errors
import com.cyclicsoft.zakatbd.FileUtil
import com.cyclicsoft.zakatbd.PreferenceHelper
import com.cyclicsoft.zakatbd.PreferenceHelper.get
import com.cyclicsoft.zakatbd.PreferenceHelper.set
import com.google.gson.Gson
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.*
import kotlin.math.abs


class HomeViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        val TAG = HomeViewModel::class.java.simpleName
    }

    private val preferences = PreferenceHelper.defaultPrefs(getApplication())
    private val _prayerTimings = MutableLiveData<Timings>()
    private val _errMsg = MutableLiveData<String>()

    val errMsg: LiveData<String>
        get() = _errMsg

    val prayerTimings: LiveData<Timings>
        get() = _prayerTimings

    private fun callTimingApi(
        location: Location,
        city: String,
        country: String,
        year: Int,
        requestCode: Int
    ) {
        Log.d(
            "$TAG : ${AppConstants.TAG_TIMING_API}",
            "callTimingApi>>> location: $location, city: $city, country: $country, year: $year, requestCode: $requestCode"
        )
        val request = ServiceBuilder.buildService(TimingDataEndPoint::class.java)
        var call: Call<TimingData>? = null
        if (requestCode == AppConstants.REQUEST_CODE_LOCATION) {
            call = request.getYearlyPrayerTimingsByLatLon(
                location.latitude,
                location.longitude,
                2,
                1,
                year,
                true
            )
        } else if (requestCode == AppConstants.REQUEST_CODE_ADDRESS) {
            call = request.getYearlyPrayerTimingsByCity(
                city,
                country,
                2,
                1,
                year,
                true
            )
        }
        if (call == null) {
            _errMsg.value = Errors.API_ERROR
            return
        }
        call.enqueue(object : Callback<TimingData> {
            override fun onResponse(call: Call<TimingData>, response: Response<TimingData>) {
                if (response.isSuccessful) {
                    val timingData = response.body()
                    if (timingData?.code == HttpURLConnection.HTTP_OK) {
                        saveTimingData(timingData, location, city, year)
                        _prayerTimings.value = timingData.data.apr.get(19).timings
                        Log.d(
                            TAG + ":" + AppConstants.TAG_TIMING_API,
                            "timing>>> ${timingData.data.apr.get(19).timings}"
                        )
                        Log.d(
                            TAG + ":" + AppConstants.TAG_TIMING_API,
                            "date>>> ${timingData.data.apr.get(19).date}"
                        )
                        Log.d(
                            TAG + ":" + AppConstants.TAG_TIMING_API,
                            "meta>>> ${timingData.data.apr.get(19).meta}"
                        )

                    } else {
                        _errMsg.value = Errors.API_ERROR
                    }
                }
            }

            override fun onFailure(call: Call<TimingData>, t: Throwable) {
                _errMsg.value = Errors.API_ERROR
            }
        })
    }

    private fun saveTimingData(
        timingData: TimingData,
        location: Location,
        city: String,
        year: Int
    ) {
        Log.d(
            "$TAG : ${AppConstants.TAG_TIMING_API}",
            "saveTimingData>>> timingData: $timingData Location: $location city: $city year: $year"
        )
        preferences[AppConstants.KEY_PRAYER_TIMING] = Gson().toJson(timingData)
        if (location.provider == Errors.UNKNOWN) {
            try {
                preferences[AppConstants.KEY_PRAYER_TIMING_LAT] =
                    timingData.data.meta.latitude.toFloat()
                preferences[AppConstants.KEY_PRAYER_TIMING_LON] =
                    timingData.data.meta.longitude.toFloat()
            } catch (exception: Exception) {
                Log.e(TAG + ":" + AppConstants.TAG_TIMING_API, "" + exception.localizedMessage)
            }
        } else {
            preferences[AppConstants.KEY_PRAYER_TIMING_LAT] = location.latitude.toFloat()
            preferences[AppConstants.KEY_PRAYER_TIMING_LON] = location.longitude.toFloat()
        }
        preferences[AppConstants.KEY_PRAYER_TIMING_CITY] = city
        FileUtil.writeJson(
            getApplication(),
            Gson().toJson(timingData.data),
            city.trim().split(" ").first() + year
        )
    }

    fun requestTimingData(
        location: Location?,
        year: Int,
        city: String,
        country: String,
        requestCode: Int
    ) {
        Log.d(
            "$TAG : ${AppConstants.TAG_TIMING_API}",
            "requestTimingData>>> location: $location?, year: $year, city: $city, country: $country, requestCode: $requestCode"
        )
        var currentCity = city
        var currentCountry = country
        val prayerTimingsCity: String? = preferences[AppConstants.KEY_PRAYER_TIMING_CITY]

        if (requestCode == AppConstants.REQUEST_CODE_LOCATION) {
            location?.let { currentLocation ->
                val prayerTimingsLat: Float? = preferences[AppConstants.KEY_PRAYER_TIMING_LAT]
                val prayerTimingsLon: Float? = preferences[AppConstants.KEY_PRAYER_TIMING_LON]
                if (prayerTimingsLat != -1f && prayerTimingsLon != -1f) {
                    val lastSavedLocation = Location("")
                    lastSavedLocation.latitude = try {
                        prayerTimingsLat!!.toDouble()
                    } catch (e: NumberFormatException) {
                        AppConstants.DEFAULT_LAT
                    }
                    lastSavedLocation.longitude = try {
                        prayerTimingsLon!!.toDouble()
                    } catch (e: NumberFormatException) {
                        AppConstants.DEFAULT_LON
                    }
                    val distance = abs(lastSavedLocation.distanceTo(location))
                    if (distance < AppConstants.DISTANCE_THRESHOLD) {
                        return
                    }
                }
                var addressList: List<Address>? = null
                try {
                    addressList = Geocoder(getApplication()).getFromLocation(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        5
                    )
                } catch (exception: Exception) {
                    _errMsg.value = Errors.CONNECTION_ERROR
                }

                currentCity = Errors.UNKNOWN
                currentCountry = Errors.UNKNOWN
                addressList?.let {
                    for (address in it) {
                        if (!address.subAdminArea.isNullOrBlank() && !address.countryName.isNullOrBlank()) {
                            currentCity = address.subAdminArea
                            currentCountry = address.countryName
                            break
                        } else {
                            continue
                        }
                    }
                }
                if (currentCity.equals(prayerTimingsCity, true)) {
                    return
                }
                callTimingApi(currentLocation, currentCity, currentCountry, year, requestCode)
            }
        } else if (requestCode == AppConstants.REQUEST_CODE_ADDRESS) {
            if (currentCity.equals(prayerTimingsCity, true)) {
                return
            }
            callTimingApi(Location(Errors.UNKNOWN), currentCity, currentCountry, year, requestCode)
        }
    }

    fun loadTimingData() {
        val timingJsonData = preferences[AppConstants.KEY_SAVED_TIMING_DATA, Errors.UNKNOWN]
        if (!timingJsonData.isNullOrBlank() || Errors.UNKNOWN != timingJsonData) {
            val timingData = Gson().fromJson(timingJsonData, TimingData::class.java)
            try {
                val timings = when (DateTime.now().monthOfYear) {
                    1 -> timingData?.data?.jan?.get(DateTime.now().dayOfMonth - 1)?.timings
                    2 -> timingData?.data?.feb?.get(DateTime.now().dayOfMonth - 1)?.timings
                    3 -> timingData?.data?.mar?.get(DateTime.now().dayOfMonth - 1)?.timings
                    4 -> timingData?.data?.apr?.get(DateTime.now().dayOfMonth - 1)?.timings
                    5 -> timingData?.data?.may?.get(DateTime.now().dayOfMonth - 1)?.timings
                    6 -> timingData?.data?.jun?.get(DateTime.now().dayOfMonth - 1)?.timings
                    7 -> timingData?.data?.jul?.get(DateTime.now().dayOfMonth - 1)?.timings
                    8 -> timingData?.data?.aug?.get(DateTime.now().dayOfMonth - 1)?.timings
                    9 -> timingData?.data?.sep?.get(DateTime.now().dayOfMonth - 1)?.timings
                    10 -> timingData?.data?.oct?.get(DateTime.now().dayOfMonth - 1)?.timings
                    11 -> timingData?.data?.nov?.get(DateTime.now().dayOfMonth - 1)?.timings
                    12 -> timingData?.data?.dec?.get(DateTime.now().dayOfMonth - 1)?.timings
                    else -> null
                }
                timings?.let {
                    var timings = formatPrayerTimes(it)
                    _prayerTimings.value = timings
                    Log.d(TAG, "Today's Timing $timings")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.localizedMessage}")
            }

        }
    }

    private fun formatPrayerTimes(timings: Timings): Timings {
        val df = DateTimeFormat.forPattern(AppConstants.TIME_FORMAT_24)
        timings.fajr = df.parseLocalTime(
            timings.fajr.split(AppConstants.TIMINGS_API_SEPARATION_CHAR).first()
                .toLowerCase(Locale.getDefault())
        ).toString(AppConstants.TIME_FORMAT_12)
        timings.dhuhr = df.parseLocalTime(
            timings.dhuhr.split(AppConstants.TIMINGS_API_SEPARATION_CHAR).first()
                .toLowerCase(Locale.getDefault())
        ).toString(AppConstants.TIME_FORMAT_12)
        timings.asr = df.parseLocalTime(
            timings.asr.split(AppConstants.TIMINGS_API_SEPARATION_CHAR).first()
                .toLowerCase(Locale.getDefault())
        ).toString(AppConstants.TIME_FORMAT_12)
        timings.maghrib = df.parseLocalTime(
            timings.maghrib.split(AppConstants.TIMINGS_API_SEPARATION_CHAR).first()
                .toLowerCase(Locale.getDefault())
        ).toString(AppConstants.TIME_FORMAT_12)
        timings.isha = df.parseLocalTime(
            timings.isha.split(AppConstants.TIMINGS_API_SEPARATION_CHAR).first()
                .toLowerCase(Locale.getDefault())
        ).toString(AppConstants.TIME_FORMAT_12)
        timings.sunrise = df.parseLocalTime(
            timings.sunrise.split(AppConstants.TIMINGS_API_SEPARATION_CHAR).first()
                .toLowerCase(Locale.getDefault())
        ).toString(AppConstants.TIME_FORMAT_12)
        timings.sunset = df.parseLocalTime(
            timings.sunset.split(AppConstants.TIMINGS_API_SEPARATION_CHAR).first()
                .toLowerCase(Locale.getDefault())
        ).toString(AppConstants.TIME_FORMAT_12)
        return timings
    }

}