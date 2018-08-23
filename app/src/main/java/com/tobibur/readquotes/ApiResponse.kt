package com.tobibur.readquotes

class ApiResponse{

    var posts: String? = null
    var error: Throwable? = null

    constructor(posts: String) {
        this.posts = posts
        this.error = null
    }

    constructor(error: Throwable) {
        this.error = error
        this.posts = null
    }
}