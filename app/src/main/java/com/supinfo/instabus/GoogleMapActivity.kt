package com.supinfo.instabus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.supinfo.instabus.data.Response
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.maps)
        setSupportActionBar(toolbar)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //items du menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_map ->{
                val intent = Intent(this, GoogleMapActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val retrofit = Retrofit.Builder()
            .baseUrl("http://barcelonaapi.marcpous.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        val api = retrofit.create(ApiService::class.java)

        api.fetchAllBus().enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                val streetForZoom = LatLng(response.body()!!.data.nearstations!![0].lat.toDouble(), response.body()!!.data.nearstations!![0].lon.toDouble())
                val tailleTableau = response.body()!!.data.nearstations!!.size -1

                for(i in 0..tailleTableau){
                    val street = LatLng(response.body()!!.data.nearstations!![i].lat.toDouble(), response.body()!!.data.nearstations!![i].lon.toDouble())
                    val zoomLevel = 15.0f
                    mMap.addMarker(MarkerOptions().position(street).title("Marker in ${response.body()!!.data.nearstations!![i].street_name}"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(streetForZoom, zoomLevel))
                }
             }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("yahia", "${t.message}")
            }
        })
    }
}