package com.example.mobiledevelopment.ui.maps

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.databinding.ActivityMapsBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val geofenceRadius = 10000.0
    private val boundsBuilder = LatLngBounds.Builder()

    private val mapsViewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        setMapStyle()
        getMyLocation()
        addManyMarker()

        mapsViewModel.getSession().observe(this){user->
            user.token?.let { mapsViewModel.getStories(it, mapsViewModel.currentLatitude, mapsViewModel.currentLongitude ) }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


            if (lastKnownLocation != null) {
                val latitude = lastKnownLocation.latitude
                val longitude = lastKnownLocation.longitude
                mapsViewModel.setCurrentLocation(latitude,longitude)

                Log.d("LAT", "Lat : ${latitude}, Lon : ${longitude}")

                val sydney = LatLng(latitude, longitude)
                mMap.addMarker(
                    MarkerOptions()
                        .position(sydney).title("My Current Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12f))
                mMap.addCircle(
                    CircleOptions()
                        .center(sydney)
                        .radius(geofenceRadius)
                        .fillColor(0x22FF0000)
                        .strokeColor(Color.RED)
                        .strokeWidth(3f)
                )
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }



    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun addManyMarker() {
        mapsViewModel.listDst.observe(this) {data ->
            data.forEach { data ->

                val latLng = LatLng(data.lat, data.lon)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(data.name)
                        .snippet(data.description)
                )
            }
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}