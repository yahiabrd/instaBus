package com.supinfo.instabus

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.supinfo.instabus.data.Data
import com.supinfo.instabus.data.NearstationsItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://barcelonaapi.marcpous.com")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.fetchAllBus().enqueue(object : Callback<com.supinfo.instabus.data.Response>{
            override fun onResponse(call: Call<com.supinfo.instabus.data.Response>, response: Response<com.supinfo.instabus.data.Response>) {
                showInformations(response.body()!!.data.nearstations!!.toList())
            }

            override fun onFailure(call: Call<com.supinfo.instabus.data.Response>, t: Throwable) {
                d("yahia", "${t.message}")
            }
        })
    }

    private fun showInformations(bus: List<NearstationsItem>) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MainRecycleAdapter(bus)
        }
    }

    fun search(view: View){
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
}