package com.snadinao.ecommerce.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.snadinao.ecommerce.MainActivity
import com.snadinao.ecommerce.R
import com.snadinao.ecommerce.databinding.ActivityVerificationBinding
import com.snadinao.ecommerce.databinding.ActivityWelcomeBinding

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding
    var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if (auth!!.currentUser != null){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        supportActionBar?.hide()
        binding.editNumber.requestFocus()
        binding.continueBtn.setOnClickListener {
            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("phone_number", binding.editNumber.text.toString())
            startActivity(intent)
        }
    }
}