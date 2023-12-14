package com.example.mobiledevelopment.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.data.response.ListDestinationItem
import com.example.mobiledevelopment.databinding.ActivityMainBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.adapter.ListDestinationAdapter
import com.example.mobiledevelopment.ui.age.AgeActivity
import com.example.mobiledevelopment.ui.category.CategoryActivity
import com.example.mobiledevelopment.ui.maps.MapsActivity
import com.example.mobiledevelopment.ui.welcome.WelcomeActivity
import com.example.mobiledevelopment.util.RetryDialog
import com.example.mobiledevelopment.util.RetryListener
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(3000)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }else {
                if (user.category.isNullOrEmpty()) {
                    startActivity(Intent(this, CategoryActivity::class.java))
                    finish()
                } else{
                    if(user.age.isNullOrEmpty()){
                        startActivity(Intent(this, AgeActivity::class.java))
                        finish()
                    }else {
                        user.token?.let { viewModel.getStories(it, viewModel.currentLatitude, viewModel.currentLongitude) }
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


        getMyLocation()
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
            R.id.logout ->{
                viewModel.logout()
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
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

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


            if (lastKnownLocation != null) {
                val latitude = lastKnownLocation.latitude
                val longitude =  lastKnownLocation.longitude


                if (latitude == 0.0 && longitude == 0.0) {
                    // Show retry dialog
                    showRetryDialog()
                } else {
                    // Set the current location in the viewModel
                    viewModel.setCurrentLocation(latitude, longitude)
                    getAddressFromLocation(this, latitude, longitude)
                }



            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    private fun showRetryDialog() {
        // Implement your retry dialog logic here
        // You can show an AlertDialog, Snackbar, or any other UI element to prompt the user to retry
        AlertDialog.Builder(this)
            .setTitle("Retry")
            .setMessage("Unable to get location. Do you want to retry?")
            .setPositiveButton("Retry") { _, _ ->
                // Retry logic, you can call getMyLocation() again or handle as needed
                getMyLocation()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                // Handle cancellation if needed
            }
            .show()
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
                    showToast("Alamat tidak ditemukan")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showToast("Gagal mendapatkan alamat")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
