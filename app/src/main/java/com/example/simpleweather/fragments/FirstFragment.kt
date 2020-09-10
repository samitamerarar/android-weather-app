package com.example.simpleweather.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.simpleweather.R
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

const val CITY_ID: String = "6050612"
const val API: String = "7ba91c6f393fb48aa05033e45d3a0b66"

class FirstFragment: Fragment() {

    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(
            R.layout.page_1, container, false)
        weatherTask().execute()
        return root
    }

    inner class weatherTask(): AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            root.findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            root.findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            root.findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            // api.openweathermap.org/data/2.5/forecast?id=$CITY_ID&units=metric&appid=$API
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?id=$CITY_ID&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            println(response)
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                )
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")

                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name")+", "+sys.getString("country")

                /* Populating extracted data into our views */
                root.findViewById<TextView>(R.id.address).text = address
                root.findViewById<TextView>(R.id.updated_at).text =  updatedAtText
                root.findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                root.findViewById<TextView>(R.id.temp).text = temp
                root.findViewById<TextView>(R.id.temp_min).text = tempMin
                root.findViewById<TextView>(R.id.temp_max).text = tempMax
                root.findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                    Date(sunrise*1000)
                )
                root.findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                    Date(sunset*1000)
                )
                root.findViewById<TextView>(R.id.wind).text = round(windSpeed.toFloat()*3.6).toInt().toString() + " km/h"

                /* Views populated, Hiding the loader, Showing the main design */
                root.findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                root.findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            } catch (e: Exception) {
                root.findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                root.findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        }
    }
}