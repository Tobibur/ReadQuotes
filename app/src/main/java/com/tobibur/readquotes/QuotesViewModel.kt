package com.tobibur.readquotes

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class QuotesViewModel : ViewModel(){

    private var quote : LiveData<ApiResponse>? = null
    private val mApiRepo: ApiRepo = ApiRepo()

    fun getQuoteData(refresh: Boolean): LiveData<ApiResponse> {
        if (refresh) {
            quote = mApiRepo.getPosts()
            return quote as LiveData<ApiResponse>
        }
        if (this.quote == null) {
            quote = mApiRepo.getPosts()
            return quote as LiveData<ApiResponse>
        }
        return quote as LiveData<ApiResponse>
    }
}