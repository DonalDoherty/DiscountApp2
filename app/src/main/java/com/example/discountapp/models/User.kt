package com.example.discountapp.models


class User(var id: String? = null,
           var userName: String? = null,
           var avatar: String? = null,
           var bio: String? = null,
           var likedPosts: Array<String>? = null,
           var uploadedPosts: Array<String>? = null,
           var score: Int? = null)
    {

    constructor(id: String, userName: String, avatar: String, bio: String,
                likedPosts: Array<String>, uploadedPosts: Array<String>, score: Int) : this() {
        this.id = id
        this.userName = userName
        this.avatar = avatar
        this.bio = bio
        this.likedPosts = likedPosts
        this.uploadedPosts = uploadedPosts
        this.score = score
    }
}