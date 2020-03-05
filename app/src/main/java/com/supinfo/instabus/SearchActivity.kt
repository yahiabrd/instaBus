package com.supinfo.instabus

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.supinfo.instabus.data.NearstationsItem
import com.supinfo.instabus.data.Response
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SearchActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        val searchView = findViewById(R.id.searchView) as SearchView
        searchView.queryHint = "Station name"
        searchView.setOnQueryTextListener(object :  SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://barcelonaapi.marcpous.com")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

                val api = retrofit.create(ApiService::class.java)

                api.fetchAllBus().enqueue(object : Callback<Response> {
                    override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                        val nameStation = query
                        val listStations = response.body()!!.data.nearstations!!.toList()
                        val existStation = listStations.find {
                            it.street_name.toLowerCase().contains(nameStation.toLowerCase())
                        }

                        if(existStation.toString() != "null"){
                            val id = existStation!!.id
                            val name = existStation!!.street_name
                            val lat = existStation!!.lat
                            val lon = existStation!!.lon
                            redirect(id, name, lat, lon)
                        }else{
                            val t = Toast.makeText(this@SearchActivity,  "Station not found", Toast.LENGTH_LONG)
                            t. show()
                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        Log.d("yahia", "${t.message}")
                    }
                })

                return false
            }

        })

    }
    fun redirect(id:String, name:String, lat:String, lon:String){
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("idStation", id)
        intent.putExtra("nameStation", name)
        intent.putExtra("lat", lat)
        intent.putExtra("lon", lon)
        startActivity(intent)
    }
}