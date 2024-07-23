package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(),OnMapReadyCallback {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        val cameraPostion = CameraPosition.Builder()
        cameraPostion.zoom(18f)
        cameraPostion.target(LatLng(40.382999588904006, 71.78275311066025))
        cameraPostion.tilt(45f)


        map.mapType = GoogleMap.MAP_TYPE_HYBRID

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPostion.build()))

        map.setOnMapClickListener {
            map.addMarker(MarkerOptions().position(it).title("New Marker"))
        }

        map.setOnMarkerClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("lat",it.position.latitude.toDouble())
            intent.putExtra("lon",it.position.longitude.toDouble())
            startActivity(intent)
            true
        }
    }
}