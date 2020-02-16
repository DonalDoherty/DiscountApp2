package com.example.discountapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userId = mAuth.currentUser!!.uid
    private val storage = FirebaseStorage.getInstance()
    private val docRef = db.collection("users").document(userId)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
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

        edit_button_profile.setOnClickListener{it ->
            showEditBio()
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
        profile_image_profile.setOnClickListener { it ->
            showEditAvatar()
        }

        }

    fun showEditAvatar(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Avatar")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        var chooseButton = Button(this)
        chooseButton.text = "Choose Photo"
        var takeButton = Button(this)
        takeButton.text = "Take Photo"
        layout.addView(chooseButton)
        layout.addView(takeButton)
        takeButton.setOnClickListener{it ->

        }
        chooseButton.setOnClickListener{it ->
            val gallery = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 0)
        }
        builder.setView(layout)
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val imageUri = data!!.data;
            if (imageUri is Uri) {
                uploadImage(imageUri)
                profile_image_profile.setImageURI(imageUri)
            }
        }
    }

    fun uploadImage(filePath: Uri) {
        val ref = storage.reference.child("images/" + userId)
        val uploadTask = ref?.putFile(filePath!!)

        val urlTask =
            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    updateUserDb(downloadUri.toString())
                }
            }
    }

    private fun updateUserDb(downloadUri :String){
        db.collection("users").document(userId).update("avatar", downloadUri)
    }
    private fun showEditBio(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Bio")
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        var newBio = EditText(this)
        layout.addView(newBio)
        builder.setView(layout)

        //add an ok button
        builder.setPositiveButton(android.R.string.ok) {dialog, p1->
            val newBioText = newBio.text
            var isValid = true
            if (newBioText.toString().isBlank()|| newBioText.toString().length>100){
                Toast.makeText(this, "Please Enter A Bio 1-100Char", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
                isValid = false
            }

            if (isValid){
                db.collection("users").document(userId).update("bio", newBioText.toString())
                finish();
                //override pending transition to make refresh seamless
                overridePendingTransition(0, 0);
                startActivity(Intent(this, ProfileActivity::class.java));
                overridePendingTransition(0, 0);
            }

            if (isValid){
                dialog.dismiss()
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }
        builder.show()
    }
}
