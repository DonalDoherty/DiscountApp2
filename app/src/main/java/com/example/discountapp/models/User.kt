package com.example.discountapp.models


class User(var userName: String? = null,
           var avatar: String? = null,
           var bio: String? = null,
           var likedPosts: List<String>? = null,
           var uploadedPosts: List<String>? = null,
           var score: Int? = null)
{

    constructor(userName: String, avatar: String, bio: String,
                likedPosts: List<String>, uploadedPosts: List<String>, score: Int) : this() {

        this.userName = userName
        this.avatar = avatar
        this.bio = bio
        this.likedPosts = likedPosts
        this.uploadedPosts = uploadedPosts
        this.score = score
    }
}