package com.example.discountapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.discountapp.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*

class MainActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var adaptedPosts = mutableListOf<Post?>()
    private var postAdapter = PostAdapter(adaptedPosts as List<Post>)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var mLayoutManager = LinearLayoutManager(this)
        recents_list_main.layoutManager = mLayoutManager
        recents_list_main.itemAnimator = DefaultItemAnimator()
        recents_list_main.adapter = postAdapter
        db.collection("posts").get().addOnSuccessListener{documents ->
            for (document in documents) {
                var postList = mutableListOf<String>()
                postList.add(document.id)
                importPosts(postList)
            }

        }
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
    fun importPosts(postList: List<String>){
        for (id in postList)
            db.collection("posts").document(id).get().addOnSuccessListener { documentSnapshot ->
                var post = documentSnapshot.toObject<Post>()
                if (post != null) {
                    post.id = id
                }
                adaptedPosts.add(post)
                this.adaptedPosts = adaptedPosts
                postAdapter.notifyDataSetChanged()
            }
    }
}

