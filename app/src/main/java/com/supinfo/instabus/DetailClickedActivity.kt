package com.supinfo.instabus

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.supinfo.instabus.DAO.StationsDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detailclicked.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class DetailClickedActivity : AppCompatActivity(){

    //menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_details, menu)
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
            R.id.action_share ->{
                share()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailclicked)
        setSupportActionBar(toolbar)

        val extras = intent.extras
        stationName.text = extras!!.getString("stationName")
        pictureName.text = extras!!.getString("pictureName")
        val pathImg = extras!!.getString("imagePicture")!!.toUri()

        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
        val imageView = ImageView(this)
        Glide.with(this).load(pathImg.toString()).into(imageView)
        linearLayout.addView(imageView)

        val file = File(pathImg.toString())
        val lastModDate = Date(file.lastModified())
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date: String = simpleDateFormat.format(lastModDate)
        datePicture.text = date

        val stationsHelper = StationsDBHelper(this)
        delete.setOnClickListener{
            stationsHelper.deleteDetailStation(pathImg.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            val myFile = File(pathImg.toString())
            if (myFile.exists()) myFile.delete()

            val t = Toast.makeText(this@DetailClickedActivity,  "successfully deleted", Toast.LENGTH_LONG)
            t. show()
        }
    }

    private fun share(){
        val extras = intent.extras
        val pathImg = extras!!.getString("imagePicture")!!.toUri()

        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/jpeg"
        share.putExtra(Intent.EXTRA_STREAM, pathImg)
        startActivity(Intent.createChooser(share, "Share Image"))
    }
}