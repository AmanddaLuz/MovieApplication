package com.amandaluz.movieapplication.view.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.amandaluz.movieapplication.databinding.ActivityUserBinding
import com.amandaluz.movieapplication.view.home.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var userId: String
    private var uriImage: Uri? = null

    private var getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            setImageFromGallery(it)
        }
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickToChoosePhoto()
        logOut()
        binding.btnHome.setOnClickListener {
            startActivity(Intent(this , HomeActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        val email: String = FirebaseAuth.getInstance().currentUser?.email.toString()

        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val documentReference = db.collection("users").document(userId)
        documentReference.addSnapshotListener { documentSnapshot, error ->
            if (documentSnapshot != null){
                binding.registerName.text = documentSnapshot.getString("name")
                binding.registerEmail.text = email
            }
        }
    }

    private fun setImageFromGallery(uri: Uri) {
        binding.registerImage.setImageURI(uri)
        uriImage = uri
    }

    private fun gallery() =
        getContent.launch("image/*")

    private fun clickToChoosePhoto() {
        binding.registerCardview.setOnClickListener {
            gallery()
        }
    }

    private fun logOut(){
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this , LoginActivity::class.java))
            finish()
        }
    }
}