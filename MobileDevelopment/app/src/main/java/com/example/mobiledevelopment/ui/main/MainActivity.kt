package com.example.mobiledevelopment.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.data.response.ListDestinationItem
import com.example.mobiledevelopment.databinding.ActivityMainBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.adapter.ListDestinationAdapter
import com.example.mobiledevelopment.ui.age.AgeActivity
import com.example.mobiledevelopment.ui.category.CategoryActivity
import com.example.mobiledevelopment.ui.maps.MapsActivity
import com.example.mobiledevelopment.ui.welcome.WelcomeActivity
import com.example.mobiledevelopment.util.setupView
import com.example.mobiledevelopment.util.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }


    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else {
                if (user.category.isNullOrEmpty()) {
                    startActivity(Intent(this, CategoryActivity::class.java))
                    finish()
                } else{
                    if(user.age == 0 || user.age == 1 ){
                        startActivity(Intent(this, AgeActivity::class.java))
                        finish()
                    }else {
                        user.token?.let { token ->
                            viewModel.currentLatitude.observe(this) { lat ->
                                viewModel.currentLongitude.observe(this) { long ->
                                        viewModel.getStories(token, lat, long, user.age, user.category)
                                }
                            }
                        }
                    }
                }
            }
        }

        viewModel.listDst.observe(this){
                consumer -> setUserList(consumer)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }


        if (!isGpsEnabled()) {
            promptEnableGps()
        }


        setupView()
        setSupportActionBar(binding.toolbarMain)

        binding.mapsImage.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showRetryLayout() {
        val inflater = LayoutInflater.from(this)
        val retryView = inflater.inflate(R.layout.retry_layout, null)
        val retryDialog = AlertDialog.Builder(this)
            .setView(retryView)
            .setCancelable(false)
            .create()

        val buttonRetry = retryView.findViewById<Button>(R.id.buttonRetry)
        buttonRetry.setOnClickListener {
            retryDialog.dismiss()
            getMyLastLocation()
        }
        retryDialog.show()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    getAddressFromLocation(this@MainActivity, location.latitude, location.longitude)
                    viewModel.setCurrentLocation(location.latitude, location.longitude)
                } else {
                    showRetryLayout()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private fun isGpsEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun promptEnableGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ageSetting -> {
                val intent = Intent(this@MainActivity, AgeActivity::class.java)
                startActivity(intent)
            }
            R.id.categorySetting -> {
                val intent = Intent(this@MainActivity, CategoryActivity::class.java)
                startActivity(intent)
            }
            R.id.logout ->{
                viewModel.logout()
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val kecamatan = addresses[0].subLocality
                    val kota = addresses[0].adminArea


                    if(kecamatan.isNullOrEmpty()){
                        val changeTitle = binding.toolbarMain
                        changeTitle.title = kota.toString()
                    }else{
                        val changeTitle = binding.toolbarMain
                        changeTitle.title = kecamatan.toString()
                    }

                } else {
                    showToast(context,"Alamat tidak ditemukan")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(context,"Gagal mendapatkan alamat")
        }
    }


    private fun setUserList(consumer: List<ListDestinationItem?>?){
        val adapter = ListDestinationAdapter()
        adapter.submitList(consumer)
        binding.rvDst.adapter = adapter
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
