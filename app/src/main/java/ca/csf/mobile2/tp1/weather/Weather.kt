package ca.csf.mobile2.tp1.weather

import android.os.Parcel
import android.os.Parcelable

data class Weather(val type : WeatherType, val temperatureInCelsius : Int, val city : String) : Parcelable {

    constructor(parcel: Parcel) : this(
        WeatherType.values()[parcel.readInt()],
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type.ordinal)
        parcel.writeInt(temperatureInCelsius)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(parcel)
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }
}