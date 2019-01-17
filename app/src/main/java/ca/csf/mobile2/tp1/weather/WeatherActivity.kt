package ca.csf.mobile2.tp1.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import ca.csf.mobile2.tp1.R
import com.beust.klaxon.Klaxon
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.HttpURLConnection
import java.net.URL
import android.net.ConnectivityManager
import android.content.Context

const val WEATHER_URL = "https://m2t1.csfpwmjv.tk/api/v1/toto"
const val STATE_WEATHER = "STATE_WEATHER"
const val STATE_ERROR_BOOLEAN = "STATE_ERROR_BOOLEAN"

class WeatherActivity : AppCompatActivity() {

    private var weather: Weather? = null

    private lateinit var temperatureTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView
    private lateinit var errorImageView: ImageView
    private lateinit var weatherPreviewImage: ImageView

    private var noInternetConnection : Boolean = false
    private var isErrorScreenShowing : Boolean = false
        get() = errorTextView.visibility == View.VISIBLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        temperatureTextView = findViewById(R.id.temperatureTextView)
        cityTextView = findViewById(R.id.cityTextView)
        progressBar = findViewById(R.id.progressBar)
        errorTextView = findViewById(R.id.errorTextView)
        errorImageView = findViewById(R.id.errorImageView)
        weatherPreviewImage = findViewById(R.id.weatherPreviewImageView)
    }

    override fun onResume() {
        super.onResume()
        if (weather == null) {
            sendRequest()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(STATE_WEATHER, weather)

        if (isErrorScreenShowing) {
            outState.putBoolean(STATE_ERROR_BOOLEAN, noInternetConnection)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        weather = savedInstanceState.getParcelable(STATE_WEATHER) ?: weather

        if (savedInstanceState.containsKey(STATE_ERROR_BOOLEAN)) {
            noInternetConnection = savedInstanceState.getBoolean(STATE_ERROR_BOOLEAN)
            showErrorScreen(noInternetConnection)
        }
        else {
            showWeather()
        }
    }

    private fun sendRequest() {
        progressBar.visibility = View.VISIBLE

        doAsync {
            val url = URL(WEATHER_URL)
            val httpClient = url.openConnection() as HttpURLConnection
            var response: String = ""

            if (!isNetworkAvailable()) {
                noInternetConnection = true
            }
            else if(httpClient.responseCode != HttpURLConnection.HTTP_OK) {
                noInternetConnection = false
            }
            else {
                response = url.readText()
            }

            uiThread {
                if (response.isNotBlank()) {
                    weather = Klaxon().parse<Weather>(response)!!
                    showWeather()
                }
                else {
                    showErrorScreen(noInternetConnection)
                }
            }
        }
    }

    private fun showWeather() {
        progressBar.visibility = View.INVISIBLE
        errorImageView.visibility = View.INVISIBLE
        errorTextView.visibility = View.INVISIBLE

        temperatureTextView.visibility = View.VISIBLE
        cityTextView.visibility = View.VISIBLE

        temperatureTextView.text = weather?.temperatureInCelsius.toString()
        cityTextView.text = weather?.city

        weatherPreviewImage.visibility = View.VISIBLE
        when(weather?.type){
            WeatherType.SUNNY -> weatherPreviewImage.setImageResource(R.drawable.ic_sunny)
            WeatherType.CLOUDY -> weatherPreviewImage.setImageResource(R.drawable.ic_cloudy)
            WeatherType.PARTLY_SUNNY -> weatherPreviewImage.setImageResource(R.drawable.ic_partly_sunny)
            WeatherType.RAIN -> weatherPreviewImage.setImageResource(R.drawable.ic_rain)
            WeatherType.SNOW -> weatherPreviewImage.setImageResource(R.drawable.ic_snow)
        }

    }

    private fun showErrorScreen(noInternetConnection : Boolean) {
        errorImageView.visibility = View.VISIBLE
        errorTextView.visibility = View.VISIBLE

        temperatureTextView.visibility = View.INVISIBLE
        cityTextView.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
        weatherPreviewImage.visibility = View.INVISIBLE

        if (noInternetConnection) {
            errorTextView.text = getString(R.string.error_connectivity)
        }
        else {
            errorTextView.text = getString(R.string.error_server)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun onRetryButtonClick(view : View) {
        sendRequest()
    }
}