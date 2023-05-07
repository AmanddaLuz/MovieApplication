package com.amandaluz.movieapplication.view.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import coil.load
import com.amandaluz.movieapplication.databinding.ActivityUserBinding
import com.amandaluz.movieapplication.view.home.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class UserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var userId : String
    private var uriImage : Uri? = null
    private var imageUrl : String? = null

    private var getContent = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            uploadImageToFirebase(it)
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

        val email : String = FirebaseAuth.getInstance().currentUser?.email.toString()

        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val documentReference = db.collection("users").document(userId)
        documentReference.addSnapshotListener { documentSnapshot , error ->
            if (documentSnapshot != null) {
                binding.registerName.text = documentSnapshot.getString("name")
                binding.registerEmail.text = email
                imageUrl = documentSnapshot.getString("imageUrl")
                imageUrl?.let { url ->
                    binding.registerImage.load(url)
                }
            }
        }
    }

    private fun uploadImageToFirebase(uri : Uri) {
        val storage = Firebase.storage
        val reference = storage.reference.child("images/avatar_user")
        val imageRef = reference.child(userId)

        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnFailureListener {
            Toast.makeText(this , "Erro ao enviar a imagem!" , Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                imageUrl = downloadUrl.toString()
                setImageFromGallery(uri)
                saveImageUrlToFirestore(imageUrl)
                Toast.makeText(this , "Imagem salva com sucesso!" , Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this ,
                    "Erro ao obter a URL de download da imagem!" ,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveImageUrlToFirestore(url : String?) {
        val documentReference = db.collection("users").document(userId)
        val userUpdates = hashMapOf(
            "imageUrl" to url
        )
        documentReference.update(userUpdates as Map<String , Any>)
    }

    private fun setImageFromGallery(uri : Uri) {
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

    private fun logOut() {
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this , LoginActivity::class.java))
            finish()
        }
    }
}
