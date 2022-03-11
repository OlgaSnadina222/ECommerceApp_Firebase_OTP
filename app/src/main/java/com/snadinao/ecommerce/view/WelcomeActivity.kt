package com.snadinao.ecommerce.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.os.HandlerCompat.postDelayed
import com.snadinao.ecommerce.MainActivity
import com.snadinao.ecommerce.R
import com.snadinao.ecommerce.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var animation: Animation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.hide()

        animation = AnimationUtils.loadAnimation(this, R.anim.left_to_right)
        binding.imageViewShop.startAnimation(animation)
        try {
            Handler().postDelayed(
                {
                    startActivity(Intent(this, VerificationActivity::class.java))
                    finish()
                },3000
            )
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}