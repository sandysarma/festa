package com.example.festa

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import java.io.File
import java.io.FileInputStream

class Imagepicker : AppCompatActivity() {

    private val itemList = mutableListOf<ClipData.Item>()
    private lateinit var adapter: ItemAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagepicker)


        /* val launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val uri = it.data?.data!!
                    // Use the uri to load the image
                    // Only if you are not using crop feature:
                    uri?.let { galleryUri ->
                        contentResolver.takePersistableUriPermission(
                            uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                    //////////////
                }
            }
        val imgbtn=findViewById<ImageView>(R.id.btn)

        imgbtn.setOnClickListener {
            ImagePicker.with(this)
                //...
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog { launcher.launch(it) }

        }

    }
    val imgbg=findViewById<ImageView>(R.id.imgbg)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
      *//*  Uri uri=data.setdata();
        imgbg.setImageURI();*//*
    }
*/

        val editText: EditText = findViewById(R.id.editText)
        val saveButton: Button = findViewById(R.id.saveButton)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        adapter = ItemAdapter(itemList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        saveButton.setOnClickListener {
            val inputText = editText.text.toString().trim()

            if (inputText.isNotEmpty()) {
                val newItem = ClipData.Item(inputText)
                itemList.add(newItem)
                adapter.notifyDataSetChanged()
                editText.text.clear()
            } else {
                Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



