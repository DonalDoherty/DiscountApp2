package com.example.discountapp.models

import java.time.LocalDate

class Post(var postCreator: String? = null,
           var imgUrl: String? = null,
           var title: String? = null,
           var category: String? = null,
           var prevPrice: Float? = null,
           var curPrice: Float? = null,
           var expDate: String? = null,
           var desc: String? = null) {

    constructor(
        postCreator: String, imgUrl: String, title: String, category: String,
        prevPrice: Float, curPrice: Float, expDate: String, desc: String
    ) : this() {
        this.postCreator = postCreator
        this.imgUrl = imgUrl
        this.title = title
        this.category = category
        this.prevPrice = prevPrice
        this.curPrice = curPrice
        this.expDate = expDate
        this.desc = desc
    }
}