package com.snadinao.ecommerce.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.snadinao.ecommerce.MainActivity
import com.snadinao.ecommerce.R
import com.snadinao.ecommerce.databinding.ActivityOtpBinding
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    var verificationId: String? = null
    var auth: FirebaseAuth? = null
    var dialog: ProgressDialog? = null

    private lateinit var binding: ActivityOtpBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        dialog?.setMessage(R.string.send_otp.toString())
        dialog?.setCancelable(false)
        dialog?.show()

        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        val phoneNumber = intent.getStringExtra("phone_number")
        binding.phoneLabel.text = "Verify $phoneNumber"

        val option = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {}

                override fun onVerificationFailed(p0: FirebaseException) {}

                override fun onCodeSent(verifyId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verifyId, forceResendingToken)
                    dialog?.dismiss()
                    verificationId = verifyId
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(option)
        binding.continueBtn.setOnClickListener {
            val code1 = binding.code1.text.toString()
            val code2 = binding.code2.text.toString()
            val code3 = binding.code3.text.toString()
            val code4 = binding.code4.text.toString()
            val code5 = binding.code5.text.toString()
            val code6 = binding.code6.text.toString()
            if (code1.trim().isEmpty()
                ||code2.trim().isEmpty()
                ||code3.trim().isEmpty()
                ||code4.trim().isEmpty()
                ||code5.trim().isEmpty()
                ||code6.trim().isEmpty()
            ){
                Toast.makeText(this, R.string.valid_code, Toast.LENGTH_SHORT).show()
            }
            val otp = code1 + code2 + code3 + code4 + code5 + code6
            val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
            auth!!.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val intent = Intent(this, SetupProfileActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    } else {
                        Toast.makeText(this, R.string.failed_code, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}