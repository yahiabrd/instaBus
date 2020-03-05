package com.supinfo.instabus

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.supinfo.instabus.DAO.StationModel
import com.supinfo.instabus.DAO.StationsDBHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.previewimg.*
import java.io.File
import java.io.IOException

class AddImgActivity : AppCompatActivity() {

    lateinit var photoPath: String
    val REQUEST_TAKE_PHOTO = 1
    var IMAGE_NOT_FOUND = false

    lateinit var stationsDBHelper: StationsDBHelper

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
        setContentView(R.layout.activity_preview)
        setSupportActionBar(toolbar)

        takePicture()

        stationsDBHelper = StationsDBHelper(this)
    }

    private fun takePicture() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null){
            var photoFile: File? = null
            try{
                photoFile = createImageFile()
            }catch (e: IOException){ }
            if(photoFile != null){
                val photoUri = FileProvider.getUriForFile(
                    this,
                    "com.supinfo.instabus.fileprovider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun createImageFile(): File? {
        val extras = intent.extras
        val nameStation = extras!!.getString("nameStation")

        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            nameStation.toString(),
            ".jpg",
            storageDir
        )
        photoPath = image.absolutePath
        intent.putExtra("photoPath", photoPath)
        return image
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
            val imageView = ImageView(this)
            Glide.with(this).load(Uri.parse(photoPath).toString()).into(imageView)
            linearLayout.addView(imageView)
        }else{
            IMAGE_NOT_FOUND = true
            val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
            val imageView = ImageView(this)
            imageView.setImageResource(R.drawable.imagenotfound)
            linearLayout.addView(imageView)
        }
    }

    fun addPicture(view:View){
        val extras = intent.extras
        val idStation = extras!!.getString("idStation")
        val photoPath = extras!!.getString("photoPath")

        if(editText.text.toString() == "") IMAGE_NOT_FOUND = true

        if(!IMAGE_NOT_FOUND) {
            var result = stationsDBHelper.insertStation(
                StationModel(
                    idStation = idStation.toString(),
                    pictureName = editText.text.toString(),
                    picturePath = photoPath.toString()
                )
            )
            val t = Toast.makeText(this@AddImgActivity,  "successfully created", Toast.LENGTH_LONG)
            t. show()
        }else{
            val t = Toast.makeText(this@AddImgActivity,  "Error...", Toast.LENGTH_LONG)
            t. show()
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}