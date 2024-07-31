package com.example.weatherapp

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.adapters.WeatherAdapter
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.models.Havo
import com.example.weatherapp.utils.NetworkHelper
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import org.json.JSONObject

private const val TAG = "WeatherActivity"
class WeatherActivity : AppCompatActivity() {
    lateinit var requestQueue: RequestQueue
    lateinit var list:ArrayList<Havo>
    lateinit var adapter: WeatherAdapter
    lateinit var networkHelper: NetworkHelper
    private val binding by lazy { ActivityWeatherBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val lat = intent.getDoubleExtra("lat",0.0)
        val lon = intent.getDoubleExtra("lon",0.0)
        var url = "https://api.weatherapi.com/v1/forecast.json?key=cd561d4bc365492682c45737242007&q=$lat,$lon&days=14"

        networkHelper = NetworkHelper(this)
        requestQueue = Volley.newRequestQueue(this)
        VolleyLog.DEBUG = true
        val dialog = ProgressDialog(this)
        dialog.setCancelable(false)
        dialog.setTitle("Yuklanmoqda")
        dialog.setMessage("Kutib turing...")
        dialog.show()


        val jsonArray =JsonObjectRequest(
            Request.Method.GET,url,null
            ,object :Response.Listener<JSONObject>{

                override fun onResponse(response: JSONObject?) {
                    dialog.dismiss()
                    Log.d(TAG, "onResponse: $response")
                    val gson = Gson()
                    val user = gson.fromJson<Havo>(response.toString(), Havo::class.java)
                    binding.location.text = user.location.name
                    binding.date.text = user.forecast.forecastday.get(0).date
                    binding.harorat.text = user.current.temp_c.toString()
                    binding.namlik.text = user.current.humidity.toString()
                    binding.shamolTezligi.text = user.current.wind_kph.toString()
                    if (user.current.condition.text == "Sunny" || user.current.condition.text == "Clear"){
                        binding.imageView.setImageResource(R.drawable.sun)
                        binding.malumot.text = "Ochiq osmon"
                    }else if (user.current.condition.text == "Light rain shower"){
                        binding.imageView.setImageResource(R.drawable.rainy)
                        binding.malumot.text = "Yomg'irli osmon"
                    }else{
                        binding.imageView.setImageResource(R.drawable.cloud)
                        binding.malumot.text = "Bulutli osmon"
                    }
                    adapter = WeatherAdapter(user.forecast.forecastday)
                    binding.rv.adapter = adapter


                }

            },object :Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d(TAG, "onErrorResponse: ${error?.message}")
                }
            }
        )
        requestQueue.add(jsonArray)

    }
}