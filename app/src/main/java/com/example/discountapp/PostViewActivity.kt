package com.example.discountapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.discountapp.models.Post
import com.example.discountapp.models.User
import kotlinx.android.synthetic.main.activity_post_view.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class PostViewActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val userId = mAuth.currentUser!!.uid
    private var postCreatorId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_view)
        var postID = intent.getStringExtra("postID")
        updateUi(postID)
        likeButton_post_view.setOnClickListener{
            if (userId == postCreatorId)
            {
                Toast.makeText(this, "Error: You can't like your own posts!", Toast.LENGTH_SHORT).show()
            }
            else
            {
                db.collection("users").document(postCreatorId).get().addOnSuccessListener { documentSnapshot ->
                    var score = documentSnapshot.get("score").toString().toInt()
                    var newScore = score + 1
                    db.collection("users").document(postCreatorId).update("score", newScore)
                    db.collection("users").document(userId).update("likedPosts", FieldValue.arrayUnion(postID.toString()))

                }
            }
        }
        deleteButton_post_view.setOnClickListener{
            if (userId == postCreatorId)
            {
                db.collection("posts").document(postID).delete()
                db.collection("users").document(userId).update("uploadedPosts", FieldValue.arrayRemove(postID.toString()))
                finish()
            }
            else
            {
                Toast.makeText(this, "Error: You can't delete someone elses post!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUi(postID: String){
        db.collection("posts").document(postID).get().addOnSuccessListener { documentSnapshot ->
            var post = documentSnapshot.toObject<Post>()
            if (post != null) {
                Picasso.get().load(post.imgUrl).into(postImage_post_view)
                title_post_view.text = post.title
                currentPrice_post_view.text = post.curPrice.toString()
                prevPrice_post_view.text = post.prevPrice.toString()
                category_post_view.text = post.category
                exp_post_view.text = post.expDate
                description_post_view.text = post.desc

                db.collection("users").document(post.postCreator.toString()).get().addOnSuccessListener {documentSnapshot
                       var user = documentSnapshot.toObject<User>()
                        if (user != null) {
                            Picasso.get().load(user.avatar).into(uploaderAvatar_post_view)
                            uploaderName_post_view.text = user.userName
                            postCreatorId = post.postCreator.toString()
                        }
                    }
            }
        }


        }


    }



