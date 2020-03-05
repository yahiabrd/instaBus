package com.supinfo.instabus

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.core.view.marginTop
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.supinfo.instabus.DAO.StationsDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detailstation.*


class DetailActivity : AppCompatActivity() {

    private lateinit var  arrayAdapter:ArrayAdapter<String>
    private var arraylist = ArrayList<String>()

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
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val extras = intent.extras
        Station.text = extras!!.getString("nameStation")

        val idStation = extras.getString("idStation")
        val stationsHelper = StationsDBHelper(this)
        val stations = stationsHelper.readAllStations(idStation.toString())

        img_map.setImageResource(R.drawable.maps)
        img_map.setOnClickListener{
            val intent = Intent(this, SelectedMapActivity::class.java)
            val lat = extras.getString("lat")
            val lon = extras.getString("lon")
            intent.putExtra("lat", lat)
            intent.putExtra("lon", lon)
            intent.putExtra("stationName", Station.text.toString())
            startActivity(intent)
        }

        stations.forEach {
            val tv_station = TextView(this)
            tv_station.textSize = 30F
            tv_station.text = it.pictureName
            tv_station.setPadding(10,30,10,10)

            val picturePath = it.picturePath.toUri()

            val img_station = ImageView(this)
            img_station.layoutParams = LinearLayout.LayoutParams(150,150)
            Glide.with(this)
                .load(it.picturePath.toString())
                .apply(RequestOptions.circleCropTransform())
                .into(img_station)
            img_station.setPadding(0,0,20,0)

            tv_station.setOnClickListener{
                val intent = Intent(this, DetailClickedActivity::class.java)
                intent.putExtra("stationName", Station.text.toString())
                intent.putExtra("pictureName", tv_station.text.toString())
                intent.putExtra("imagePicture", picturePath.toString())
                startActivity(intent)
            }
            img_station.setOnClickListener{
                val intent = Intent(this, DetailClickedActivity::class.java)
                intent.putExtra("stationName", Station.text.toString())
                intent.putExtra("pictureName", tv_station.text.toString())
                intent.putExtra("imagePicture", picturePath.toString())
                startActivity(intent)
            }


            val ll = LinearLayout(this)
            ll.addView(img_station)
            ll.addView(tv_station)
            this.ll_entries.addView(ll)

        }
    }

    fun takePicture(view: View){
        val extras = intent.extras
        val idStation = extras!!.getString("idStation")
        val nameStation = extras!!.getString("nameStation")
        val intent = Intent(this, AddImgActivity::class.java)
        intent.putExtra("idStation", idStation)
        intent.putExtra("nameStation", nameStation)
        startActivity(intent)
    }
}