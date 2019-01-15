package ca.csf.mobile2.tp1.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import ca.csf.mobile2.tp1.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

class WeatherActivity : AppCompatActivity() {

    companion object {
        const val URL = "https://m2t1.csfpwmjv.tk/api/v1/weather"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val temperatureTextView = findViewById<TextView>(R.id.temperatureTextView)
        temperatureTextView.visibility = View.VISIBLE

        var weather : Weather? = null

        val queue = Volley.newRequestQueue(this)
        val stringRequest = StringRequest(Request.Method.GET, URL,
            Response.Listener<String> { response ->
                weather = Klaxon().parse<Weather>(response)
                temperatureTextView.text = weather?.temperatureInCelsius.toString()
            },
            Response.ErrorListener { /*Didnt work*/ })

        queue.add(stringRequest)
    }

}