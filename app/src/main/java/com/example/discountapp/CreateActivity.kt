package com.example.discountapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.net.toUri
import com.example.discountapp.models.Post
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_create.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.text.FieldPosition

class CreateActivity : AppCompatActivity() {
    private var imageUri:Uri? = null
    private var downloadUri:Uri? = null
    private val categories = arrayOf("Fruit&Veg", "Meat", "Home Care", "Convenience", "Snacks", "Beverages", "Personal Care", "Other")
    private val mAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userId = mAuth.currentUser!!.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        //categories for spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        category_spinner_create.adapter = adapter

        cancel_button_create.setOnClickListener { it ->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        select_photo_create.setOnClickListener{it ->
            val gallery = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, 0)
        }
        post_button_create.setOnClickListener{it ->
            val title = post_title_create.text.toString()
            val category = category_spinner_create.selectedItem.toString()
            val prevPrice = prev_price_create.text.toString().toFloat()
            val currentPrice = current_price_create.text.toString().toFloat()
            val expDate = post_expiry_create.text.toString()
            val description = post_desc_create.text.toString()

            if(title.isEmpty() || category.isEmpty() || prevPrice.isNaN() || currentPrice.isNaN() || expDate
                    .isEmpty() || description.isEmpty()|| imageUri == null) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            createPost(userId, imageUri.toString(), title, category, prevPrice, currentPrice, expDate, description)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            val imageUri = data!!.data;
            this.imageUri = imageUri
            if (imageUri is Uri)
            {
                post_image_create.setImageURI(imageUri)
            }
        }
    }
    private fun createPost(postCreator: String, imagePath: String, title: String, category: String, prevPrice: Float, currentPrice: Float, expDate: String, description: String)
    {
     //creates post document in posts collection, adds the generated uuid to the users created posts list
        //upload image to storage
        var newUri = Uri.parse(imagePath)
        uploadImage(newUri)
        val post = Post(postCreator, downloadUri.toString(), title, category, prevPrice, currentPrice, expDate, description)
        db.collection("posts")
            .add(post)
                //takes post id and adds it to current users uploaded posts list

            .addOnSuccessListener {documentReference ->
                db.collection("users").document(userId).get().addOnSuccessListener { document ->
                    var uploadedPosts = document.get("likedPosts") as MutableList<String>
                    uploadedPosts.add(documentReference.id)
                    db.collection("users").document(userId).update("uploadedPosts", uploadedPosts)
                }
            }
    }

    fun uploadImage(filePath: Uri) {
        val ref = storage.reference.child("posts/" + userId)
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
                    this.downloadUri = downloadUri
                }
            }
    }
}


