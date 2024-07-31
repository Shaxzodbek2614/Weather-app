package com.example.weatherapp

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
    var latLng: LatLng? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


    }
    private fun checkLocationPermission() {
        when {
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // If permission granted, get location
                getLastLocation()
            }
            else -> {
                // Request permission
                requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // If permission granted, get location
            getLastLocation()
        } else {
            // Permission denied, handle accordingly
        }
    }
    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latLng = LatLng(location.latitude, location.longitude)
                    if (::map.isInitialized) {
                        updateMapLocation()
                    }
                }
            }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        map.mapType = GoogleMap.MAP_TYPE_HYBRID
        checkLocationPermission()

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
    private fun updateMapLocation() {
        latLng?.let {
            val cameraPosition = CameraPosition.Builder()
                .target(it)
                .zoom(18f)
                .tilt(45f)
                .build()
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
}