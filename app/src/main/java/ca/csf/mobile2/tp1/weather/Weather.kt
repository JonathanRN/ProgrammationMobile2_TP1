package ca.csf.mobile2.tp1.weather

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Weather(val type : String, val temperatureInCelsius : Int, val city : String) : Parcelable