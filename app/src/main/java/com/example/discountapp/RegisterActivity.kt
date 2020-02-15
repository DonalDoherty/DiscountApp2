package com.example.discountapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import com.example.discountapp.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

class RegisterActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val docReference = db.collection("users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //register with details provided
        register_button_register.setOnClickListener{
            val username = username_field_register.text.toString()
            val email = email_field_register.text.toString()
            val password = password_field_register.text.toString()
            //validation
            if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (!it.isSuccessful){
                        Toast.makeText(this, "Error: Try Again", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    else if(it.isSuccessful) {
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                        //create user with provided details
                        createUser(username)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
        }
        //open register page
        login_button_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    //check if user is signed in, if they are, create database info for them'
    fun createUser(username: String){
        val currentUser = mAuth.currentUser
        if (currentUser != null){
            //create collection for userdata in Firestore
            //first create user object
            val userId = currentUser.uid
            val user = User(username,
                "https://firebasestorage.googleapis.com/v0/b/discountapp-e8c10.appspot.com/o/hotdog.png?alt=media&token=b3594219-c1f1-424e-8eda-e3ba33047f7c",
                "Hi, I am a discount app user!",
                emptyList(),
                emptyList(),
                0)
            docReference.document(userId).set(user)
        } else {
            createUser(username)
        }
    }
}
