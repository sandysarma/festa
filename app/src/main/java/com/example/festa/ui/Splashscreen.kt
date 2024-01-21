package com.example.festa.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.festa.R
import com.example.festa.view.logins.ui.SignInActivity
import com.example.festa.application.Festa
import com.example.festa.databinding.ActivitySplashscreenBinding
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@AndroidEntryPoint
class Splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    private lateinit var activity: Activity
    lateinit var splashAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this

        splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        binding.SplashScreenImage.animation = splashAnimation
        try {
            val info = packageManager.getPackageInfo(
                "com.app.food",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }


        splashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                if (Festa.encryptedPrefs.isFirstTime) {
                    startActivity(Intent(this@Splashscreen, SignInActivity::class.java))

                    finish()
                    //  splashAnimation.repeatMode = 1
                    Log.e("FirstTimeLog", "1")
                } else {
                    val userId = Festa.encryptedPrefs.UserId

                    Log.e("FirstTimeLog", "2")

                    if (Festa.encryptedPrefs.isNotification && userId != "") {
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent= Intent(this@Splashscreen, DashboardActivity::class.java)
                            intent.putExtra("login","login")

                            startActivity(intent)
                            finish()
                        }, 1000)
                    }
                    else
                    {
                        val intent= Intent(this@Splashscreen, SignInActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
    }
}