package com.example.mobiledevelopment.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiledevelopment.R
import com.example.mobiledevelopment.data.Hero
import com.example.mobiledevelopment.data.ListHeroAdapter
import com.example.mobiledevelopment.databinding.ActivityMainBinding
import com.example.mobiledevelopment.ui.ViewModelFactory
import com.example.mobiledevelopment.ui.maps.MapsActivity
import com.example.mobiledevelopment.ui.welcome.WelcomeActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }


    private lateinit var binding: ActivityMainBinding
    private lateinit var rvHeroes: RecyclerView
    private val list = ArrayList<Hero>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }


        binding.mapsImage.setOnClickListener{
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
        }

        getMyLocation()


        setupView()
        setSupportActionBar(binding.toolbarMain)

        rvHeroes = findViewById(R.id.rv_heroes)

        rvHeroes.setHasFixedSize(true)

        list.addAll(listHeroes)
        showRecyclerList()
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
                val longitude = lastKnownLocation.longitude
                getAddressFromLocation(this, latitude, longitude)


            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
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

    private val listHeroes: ArrayList<Hero>
        get(){
            val dataName = resources.getStringArray(R.array.data_name)
            val dataDesc = resources.getStringArray(R.array.data_description)
            val dataPhoto = resources.getStringArray(R.array.data_photo)
            val listHero = ArrayList<Hero>()
            for(i in dataName.indices){
                val hero = Hero(dataName[i], dataDesc[i], dataPhoto[i])
                listHero.add(hero)
            }
            return listHero
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


    private fun showRecyclerList(){
        rvHeroes.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = ListHeroAdapter(list)
        rvHeroes.adapter = listHeroAdapter

        // data callback dapat dikonsumsi dari Activity dengan memanggil fungsi setOnItemClickCallback seperti berikut.
        listHeroAdapter.setOnClickedCallback(object : ListHeroAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Hero) {
                showSelectedHero(data)
            }
        })

    }

    // Mengambil data callback
    private fun showSelectedHero(hero:Hero){
        Toast.makeText(this, "Kamu Memilih  " +hero.name, Toast.LENGTH_SHORT).show()
    }
}
