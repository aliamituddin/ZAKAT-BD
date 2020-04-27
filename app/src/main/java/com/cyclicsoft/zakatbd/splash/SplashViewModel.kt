package com.cyclicsoft.zakatbd.splash

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cyclicsoft.zakatbd.*
import com.cyclicsoft.zakatbd.PreferenceHelper.get
import com.cyclicsoft.zakatbd.PreferenceHelper.set
import com.cyclicsoft.zakatbd.home.model.TimingData
import com.google.gson.Gson
import org.joda.time.DateTime

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        val TAG = SplashViewModel::class.java.simpleName
    }

    private val preferences = PreferenceHelper.defaultPrefs(getApplication())
    private var _isDataLoaded = MutableLiveData<Boolean>()

    val isDataLoaded: LiveData<Boolean>
        get() = _isDataLoaded

    private fun loadInitialTimingFile() {
        val savedTimingFile = preferences[AppConstants.KEY_SAVED_TIMING_FILE, Errors.UNKNOWN]
        if (savedTimingFile == Errors.UNKNOWN) {
            val timingDataFile =
                FileUtil.unzipJsonFromRaw(getApplication(), AppConstants.DEFAULT_TIMING_DATA_FILE)
            if (Errors.UNKNOWN != timingDataFile) {
                preferences[AppConstants.KEY_SAVED_TIMING_FILE] = timingDataFile
                preferences[AppConstants.KEY_SAVED_TIMING_DATA] =
                    FileUtil.readJson(getApplication(), timingDataFile)
            }
        }
    }

    fun loadData() {
        loadInitialTimingFile()
        _isDataLoaded.value = true
    }
}