package com.snadinao.ecommerce.view

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.snadinao.ecommerce.MainActivity
import com.snadinao.ecommerce.databinding.ActivitySetupProfileBinding
import com.snadinao.ecommerce.model.User
import kotlinx.android.synthetic.main.activity_setup_profile.*
import java.util.*
import kotlin.collections.HashMap

class SetupProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupProfileBinding
    var auth: FirebaseAuth? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var selectedImage: Uri? = null
    var dialog: ProgressDialog? = null
    var firebaseFirestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = ProgressDialog(this)
        dialog!!.setMessage("Updating profile...")
        dialog!!.setCancelable(false)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        supportActionBar?.hide()

        imView.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 200)
        }

        nameContinue.setOnClickListener(View.OnClickListener {
            val name = userName.text.toString()
            if (name.isEmpty()){
                userName.error = "Please enter your name"
                return@OnClickListener
            }
            dialog!!.show()
            if (selectedImage != null){
                val reference = storage!!.reference
                    .child("Profiles")
                    .child(auth?.uid!!)
                reference.putFile(selectedImage!!).addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        reference.downloadUrl.addOnSuccessListener { uri ->
                            val imageUri = uri.toString()
                            val uid = auth?.uid
                            val phone = auth?.currentUser?.phoneNumber
                            val name = userName.text.toString()
                            val user =User(uid, name, phone, imageUri)
                            firebaseFirestore!!.collection("USERS")
                                .add(user).addOnCompleteListener(object: OnCompleteListener<DocumentReference>{
                                    override fun onComplete(task: Task<DocumentReference>) {
                                        dialog?.dismiss()
                                        val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                })
                            database!!.reference
                                .child("users")
                                .child(uid!!)
                                .setValue(user)
                                .addOnSuccessListener {
                                    dialog!!.dismiss()
                                    val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                        }
                    }
                }
            } else {
                val uid = auth!!.uid
                val phone = auth!!.currentUser?.phoneNumber
                val user = User(uid, name, phone, "No phone")
                firebaseFirestore!!.collection("USERS")
                    .add(user).addOnCompleteListener(object: OnCompleteListener<DocumentReference>{
                        override fun onComplete(task: Task<DocumentReference>) {
                            dialog?.dismiss()
                            val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
                database!!.reference
                    .child("users")
                    .child(uid!!)
                    .setValue(user)
                    .addOnSuccessListener {
                        dialog!!.dismiss()
                        val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null){
            if (data.data != null){
                val uri = data.data
                val storage = FirebaseStorage.getInstance()
                val time = Date().time
                val reference = storage.reference
                    .child("Profiles")
                    .child(time.toString() + "")
                reference.putFile(uri!!).addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        reference.downloadUrl.addOnSuccessListener { uri ->
                            val filePath = uri.toString()
                            val obj = HashMap<String, Any>()
                            obj["image"] = filePath
                            database!!.reference
                                .child("users")
                                .child(FirebaseAuth.getInstance().uid!!)
                                .updateChildren(obj)

                        }
                    }

                }

                imView.setImageURI(data.data)
                selectedImage = data.data
            }
        }
    }
}