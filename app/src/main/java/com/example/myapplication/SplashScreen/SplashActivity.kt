package com.example.myapplication.SplashScreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.Login.data.model.UserUtil
import com.example.myapplication.Login.ui.activity.LoginActivity
import com.example.myapplication.QC.ui.activity.QcActivity
import com.example.myapplication.R
import java.util.Calendar

class SplashActivity : AppCompatActivity() {

    private val timeOut = 2000

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        var copyright = findViewById<TextView>(R.id.copyright)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        copyright.text = "© $currentYear G.B | All rights reserved"


        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

       // HERE WE ARE TAKING THE REFERENCE OF OUR IMAGE
        // SO THAT WE CAN PERFORM ANIMATION USING THAT IMAGE
        val backgroundImage: ImageView = findViewById(R.id.imageViewAnimation)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        backgroundImage.startAnimation(slideAnimation)

        Handler().postDelayed(Runnable {
            val user =  UserUtil.getCurrentUser(applicationContext);
            if (user.id == ""){
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()

            }else{
                val intent = Intent(this, QcActivity::class.java)//menu
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }, timeOut.toLong())

    }

}
