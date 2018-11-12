package com.tobibur.readquotes

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class ApiRepo {

    private val client = AsyncHttpClient()
    private val quoteURL = "http://api.forismatic.com/api/1.0/"

    fun getPosts(): LiveData<ApiResponse> {
        val apiResponse = MutableLiveData<ApiResponse>()

        client.get(quoteURL, getParams(), object : JsonHttpResponseHandler(){
            @SuppressLint("SetTextI18n")
            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: JSONObject) {
                super.onSuccess(statusCode, headers, response)
                val jsonResponse = response.toString()
                val quoteText = response.getString("quoteText")
                val author = response.getString("quoteAuthor")
                val result = "$quoteText \n\n-$author"
                apiResponse.postValue(ApiResponse(result))
                Log.i("MainActivity", "onSuccess: $jsonResponse")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, throwable: Throwable, errorResponse: JSONObject) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Log.e("MainActivity", "onFailure: $errorResponse")
                apiResponse.postValue(ApiResponse(throwable))
            }
        })

        return apiResponse
    }

    private fun getParams(): RequestParams {
        val params = RequestParams()
        params.put("method","getQuote")
        params.put("format","json")
        params.put("lang","en")
        return params
    }

}