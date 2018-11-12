package com.tobibur.readquotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class QuotesViewModel : ViewModel(){

    private var quote : LiveData<ApiResponse>? = null
    private val mApiRepo: ApiRepo = ApiRepo()

    fun getQuoteData(refresh: Boolean): LiveData<ApiResponse> {

        if (this.quote == null|| refresh) {
            quote = mApiRepo.getPosts()
            return quote as LiveData<ApiResponse>
        }
        return quote as LiveData<ApiResponse>
    }
}