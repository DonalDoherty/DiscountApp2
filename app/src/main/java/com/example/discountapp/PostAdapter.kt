package com.example.discountapp


import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContextCompat.startActivity
import com.example.discountapp.models.Post
import com.example.discountapp.R.layout.recycler_list_rows
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_list_rows.view.*


class PostAdapter(private val postsList: List<Post>) :
    RecyclerView.Adapter<PostAdapter.MyViewHolder>() {
    var currentimageId = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(recycler_list_rows, parent, false)
        return MyViewHolder(itemView, postsList)
    }

    class MyViewHolder(view: View, private val postsList: List<Post>) : RecyclerView.ViewHolder(view){
        var postID = ""
        init{
            view.setOnClickListener{

                val intent = Intent(view.context, PostViewActivity::class.java).putExtra("postID", postID )
                view.context.startActivity(intent)
            }
        }
        var thumbnail = view.post_thumb_row
        var title = view.post_title_row
        var prevPrice = view.post_prevPrice_row
        var currentPrice = view.post_currentPrice_row
        var expDate = view.post_expiry_row
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val post = postsList[position]
        Picasso.get().load(post.imgUrl).into(holder.thumbnail)
        holder.title.setText(post.title)
        holder.prevPrice.setText(post.prevPrice.toString())
        holder.currentPrice.setText(post.curPrice.toString())
        holder.expDate.setText(post.expDate.toString())
        holder.postID = post.id.toString()
    }

    override fun getItemCount(): Int {
        return postsList.size
    }



}