package com.cyclicsoft.zakatbd

import com.cyclicsoft.zakatbd.home.model.Timings

object AppConstants {
    const val DEFAULT_TIMING_DATA_FILE = "dhaka2020"
    const val KEY_IS_FIRST_LAUNCH = "KEY_IS_FIRST_LAUNCH"


    /** ---------- Prayer Timings Start---------- **/
    //TAGS
    const val TAG_TIMING_API = "TIMING_API"

    //Request Codes
    const val REQUEST_CODE_ADDRESS = 701
    const val REQUEST_CODE_LOCATION = 702

    //Default LatLon
    const val DEFAULT_LAT = 23.6850
    const val DEFAULT_LON = 90.3563

    //Others
    const val KEY_PRAYER_TIMING = "KEY_PRAYER_TIMING"
    const val KEY_PRAYER_TIMING_LAT = "KEY_PRAYER_TIMING_LAT"
    const val KEY_PRAYER_TIMING_LON = "KEY_PRAYER_TIMING_LON"
    const val KEY_PRAYER_TIMING_CITY = "KEY_PRAYER_TIMING_CITY"
    const val KEY_SAVED_TIMING_DATA = "KEY_SAVED_TIMING_DATA"
    const val KEY_SAVED_TIMING_FILE = "KEY_SAVED_TIMING_FILE"
    const val DISTANCE_THRESHOLD = 20 * 1000f//metres
    const val TIME_FORMAT_24 = "HH:mm"
    const val TIME_FORMAT_12 = "hh:mm aa"
    const val TIMINGS_API_SEPARATION_CHAR = " "
    /** ---------- Prayer Timings End---------- **/

}