package com.example.discountapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val userId = mAuth.currentUser!!.uid
        val docRef = db.collection("users").document(userId)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                //get and display avatar using picasso
                var avatar = document.get("avatar")
                if (avatar is String){
                    Picasso.get().load(avatar).into(profile_image_profile)
                }
                var bio = document.get("bio")
                if (bio is String) {
                    profile_bio_profile.text = bio
                }
                var likedPosts = document.get("likedPosts")
                var score = document.get("score")
                if (score is Number){
                    var scoreText = score.toString()
                    profile_score_profile.text = scoreText
                }
                var uploadedPosts = document.get("uploadedPosts")
                var userName = document.get("userName")
                if (userName is String){
                    profile_username_profile.text = userName
                }
            } else {
                Toast.makeText(this, "Error: Try Again", Toast.LENGTH_SHORT).show()
                finish()

            }
        }

        logout_button_profile.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

        main_button_profile.setOnClickListener{it ->
            finish()
        }
    }

}
