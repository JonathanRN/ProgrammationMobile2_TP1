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

class WeatherActivity : AppCompatActivity() {

    companion object {
        const val WEATHER_URL = "https://m2t1.csfpwmjv.tk/api/v1/weather"
    }

    var weather : Weather? = null
    var temperatureTextView : TextView? = null
    var cityTextView : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        temperatureTextView = findViewById(R.id.temperatureTextView)
        cityTextView = findViewById(R.id.cityTextView)

        sendRequest()
    }


    fun sendRequest() {
        doAsync {
            val response = URL(WEATHER_URL).readText()
            uiThread {
                if (response.isNotBlank()) {
                    temperatureTextView?.visibility = View.VISIBLE
                    cityTextView?.visibility = View.VISIBLE

                    weather = Klaxon().parse<Weather>(response)!!
                    temperatureTextView?.text = weather?.temperatureInCelsius.toString()
                    cityTextView?.text = weather?.city
                }
            }
        }
    }
}