package com.cyclicsoft.zakatbd.home.model

import com.google.gson.annotations.SerializedName

data class TimingData(

    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: Data
)

data class Data(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta,
    @SerializedName("1") val jan: List<Jan>,
    @SerializedName("2") val feb: List<Feb>,
    @SerializedName("3") val mar: List<Mar>,
    @SerializedName("4") val apr: List<Apr>,
    @SerializedName("5") val may: List<May>,
    @SerializedName("6") val jun: List<Jun>,
    @SerializedName("7") val jul: List<Jul>,
    @SerializedName("8") val aug: List<Aug>,
    @SerializedName("9") val sep: List<Sep>,
    @SerializedName("10") val oct: List<Oct>,
    @SerializedName("11") val nov: List<Nov>,
    @SerializedName("12") val dec: List<Dec>
)

data class Jan(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Feb(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Mar(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Apr(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class May(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Jun(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Jul(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Aug(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Sep(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Oct(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Nov(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Dec(

    @SerializedName("timings") val timings: Timings,
    @SerializedName("date") val date: Date,
    @SerializedName("meta") val meta: Meta
)

data class Timings(

    @SerializedName("Fajr") var fajr: String,
    @SerializedName("Sunrise") var sunrise: String,
    @SerializedName("Dhuhr") var dhuhr: String,
    @SerializedName("Asr") var asr: String,
    @SerializedName("Sunset") var sunset: String,
    @SerializedName("Maghrib") var maghrib: String,
    @SerializedName("Isha") var isha: String,
    @SerializedName("Imsak") var imsak: String,
    @SerializedName("Midnight") var midnight: String
)

data class Date(

    @SerializedName("readable") val readable: String,
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("hijri") val hijri: Hijri,
    @SerializedName("gregorian") val gregorian: Gregorian
)

data class Meta(

    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("method") val method: Method,
    @SerializedName("latitudeAdjustmentMethod") val latitudeAdjustmentMethod: String,
    @SerializedName("midnightMode") val midnightMode: String,
    @SerializedName("school") val school: String,
    @SerializedName("offset") val offset: Offset
)

data class Hijri(

    @SerializedName("date") val date: String,
    @SerializedName("format") val format: String,
    @SerializedName("day") val day: Int,
    @SerializedName("weekday") val weekday: Weekday,
    @SerializedName("month") val month: Month,
    @SerializedName("year") val year: Int,
    @SerializedName("designation") val designation: Designation,
    @SerializedName("holidays") val holidays: List<String>
)

data class Gregorian(

    @SerializedName("date") val date: String,
    @SerializedName("format") val format: String,
    @SerializedName("day") val day: Int,
    @SerializedName("weekday") val weekday: Weekday,
    @SerializedName("month") val month: Month,
    @SerializedName("year") val year: Int,
    @SerializedName("designation") val designation: Designation
)

data class Method(

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("params") val params: Params
)

data class Offset(

    @SerializedName("Imsak") val imsak: Int,
    @SerializedName("Fajr") val fajr: Int,
    @SerializedName("Sunrise") val sunrise: Int,
    @SerializedName("Dhuhr") val dhuhr: Int,
    @SerializedName("Asr") val asr: Int,
    @SerializedName("Maghrib") val maghrib: Int,
    @SerializedName("Sunset") val sunset: Int,
    @SerializedName("Isha") val isha: String,
    @SerializedName("Midnight") val midnight: Int
)

data class Weekday(

    @SerializedName("en") val en: String,
    @SerializedName("ar") val ar: String
)

data class Month(

    @SerializedName("number") val number: Int,
    @SerializedName("en") val en: String,
    @SerializedName("ar") val ar: String
)

data class Designation(

    @SerializedName("abbreviated") val abbreviated: String,
    @SerializedName("expanded") val expanded: String
)

data class Params(

    @SerializedName("Fajr") val fajr: Double,
    @SerializedName("Isha") val isha: String
)