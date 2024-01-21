package com.example.festa

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val favoriteList = ArrayList<String>()
    lateinit var adapter: Adapter
    lateinit var drawerLayout: DrawerLayout
    lateinit var leftDrawerMenu: DrawerLayout
    lateinit var rightDrawerMenu: DrawerLayout
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val ivNavMenu: ImageView =findViewById(R.id.ivNavMenu)
        val ivFavoriteList: ImageView =findViewById(R.id.ivNavMenu)
        val drawerLayout:DrawerLayout=findViewById(R.id.drawerLayout)
        leftDrawerMenu=findViewById(R.id.leftDrawerMenu)
        rightDrawerMenu=findViewById(R.id.rightDrawerMenu)
        // set on click listener to left navigation icon
        ivNavMenu.setOnClickListener {
            toggleLeftDrawer()
        }
        // set on click listener to right navigation icon
        ivFavoriteList.setOnClickListener {
            toggleRightDrawer()
        }
    }
    private fun toggleLeftDrawer() {
        if (drawerLayout.isDrawerOpen(leftDrawerMenu)) {
            drawerLayout.closeDrawer(leftDrawerMenu)
        } else {
            drawerLayout.openDrawer(leftDrawerMenu)
        }
    }
    private fun toggleRightDrawer() {
        if (drawerLayout.isDrawerOpen(rightDrawerMenu)) {
            drawerLayout.closeDrawer(rightDrawerMenu)
        } else {
            drawerLayout.openDrawer(rightDrawerMenu)
        }
    }
}