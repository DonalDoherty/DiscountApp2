package com.example.discountapp.models

import java.time.LocalDate

class Post(var id: String? = null,
           var imgUrl: String? = null,
           var title: String? = null,
           var category: String? = null,
           var curPrice: Int? = null,
           var prevPrice: Int? = null,
           var desc: String? = null,
           var expDate: LocalDate? = null)
           {

            constructor(id: String, imgUrl: String, title: String, category: String,
                        curPrice: Int, prevPrice: Int, desc: String, expDate: LocalDate) : this()
            {
                        this.id = id
                        this.imgUrl = imgUrl
                        this.title = title
                        this.category = category
                        this.curPrice = curPrice
                        this.prevPrice = prevPrice
                        this.desc = desc
                        this.expDate = expDate
            }
            }