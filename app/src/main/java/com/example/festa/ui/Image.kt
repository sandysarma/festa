package com.example.festa.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.widget.Button
import android.widget.ImageSwitcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.R
import com.example.festa.adapters.GalleryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Image : AppCompatActivity() {
    var pick_image: ImageSwitcher? = null
    var mArrayUri: ArrayList<Uri>? = null

    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f
    var galleryAdapter: GalleryAdapter? = null
    var uri = ArrayList<Uri>()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
       val picRecyclerView = findViewById<RecyclerView>(R.id.picRecyclerView)
        val pickImageBtn = findViewById<Button>(R.id.pickImageBtn)
        mArrayUri = ArrayList()
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        // RecycllerViewGallery = findViewById(R.id.RecycllerViewGallery);
       // galleryAdapter = GalleryAdapter(uri,this@Image)
        val linearLayoutManager = LinearLayoutManager(
            applicationContext, LinearLayoutManager.HORIZONTAL, false
        )
        picRecyclerView.setLayoutManager(linearLayoutManager)
        picRecyclerView.setAdapter(galleryAdapter)
        if (ContextCompat.checkSelfPermission(
                this@Image,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@Image,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Read_Permission
            )
        }
        pickImageBtn.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "select Picture"), 1)
        })
        if (ContextCompat.checkSelfPermission(
                this@Image,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@Image,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Read_Permission
            )
        }
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        scaleGestureDetector!!.onTouchEvent(motionEvent)
        return true
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f))
            pick_image!!.scaleX = mScaleFactor
            pick_image!!.scaleY = mScaleFactor
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       try {

        if (requestCode == 1 && resultCode == RESULT_OK) {


                if (data!!.clipData != null) {
                    val x = data.clipData!!.itemCount
                    //uri.clear()
                    for (i in 0 until x) {
                        uri.add(data.clipData!!.getItemAt(i).uri)
                    }
                    galleryAdapter!!.notifyDataSetChanged()
                } else if (data.data != null) {
                    val imageUrl = data.data!!.path
                    uri.add(Uri.parse(imageUrl))
                }else if(data.data == null){
                   // Toast.makeText(this,)
                }

        }

       }catch (e:IndexOutOfBoundsException){

       }
    }
    private fun removeLastImage() {
        try {


        if (uri.isNotEmpty()) {
            uri.removeAt(uri.size - 1)
            galleryAdapter?.notifyDataSetChanged()
        }
        }catch (e:IndexOutOfBoundsException){

        }
    }

    // Add a button click listener to trigger the removal
    fun onRemoveLastImageClick(view: View) {
        removeLastImage()
    }


    companion object {
        private const val Read_Permission = 101
    }
}