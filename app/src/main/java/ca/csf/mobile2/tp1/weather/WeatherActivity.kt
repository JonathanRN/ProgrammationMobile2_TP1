package ca.csf.mobile2.tp1.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import ca.csf.mobile2.tp1.R
import com.beust.klaxon.Klaxon
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

const val WEATHER_URL = "https://m2t1.csfpwmjv.tk/api/v1/weather"
const val STATE_WEATHER = "STATE_WEATHER"

class WeatherActivity : AppCompatActivity() {

    private lateinit var weather: Weather
    private lateinit var temperatureTextView: TextView
    private lateinit var cityTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        temperatureTextView = findViewById(R.id.temperatureTextView)
        cityTextView = findViewById(R.id.cityTextView)

        sendRequest()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(STATE_WEATHER, weather)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        weather = savedInstanceState.getParcelable(STATE_WEATHER) ?: weather
        showWeather()
    }

    private fun sendRequest() {
        doAsync {
            val response = URL(WEATHER_URL).readText()
            uiThread {
                if (response.isNotBlank()) {
                    weather = Klaxon().parse<Weather>(response)!!

                    if (response.isNotBlank()) {
                        showWeather()
                    }
                }
            }
        }
    }

    private fun showWeather() {
        temperatureTextView.visibility = View.VISIBLE
        cityTextView.visibility = View.VISIBLE

        temperatureTextView.text = weather.temperatureInCelsius.toString()
        cityTextView.text = weather.city
    }

    fun onRetryButtonClick(view : View) {
        sendRequest()
    }
}