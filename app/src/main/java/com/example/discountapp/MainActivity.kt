package com.example.discountapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //check if user is logged in, if not, send to login screen
        if (mAuth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        profile_button_main.setOnClickListener{it ->
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        search_button_main.setOnClickListener{it ->
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        post_create_main.setOnClickListener{it->
            val intent = Intent(this, CreateActivity::class.java )
            startActivity(intent)
        }
    }
}

