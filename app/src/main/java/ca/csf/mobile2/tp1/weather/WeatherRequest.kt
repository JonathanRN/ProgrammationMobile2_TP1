package ca.csf.mobile2.tp1.weather

import com.beust.klaxon.Klaxon
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class WeatherRequest {

    var weather : Weather? = null

    fun sendRequest() {
        doAsync {
            val response = URL(WeatherActivity.WEATHER_URL).readText()
            uiThread {
                weather = Klaxon().parse<Weather>(response)!!
                println(weather?.temperatureInCelsius)
                println(weather?.city)
                println(weather?.type)
            }
        }
    }
}