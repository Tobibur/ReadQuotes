package com.tobibur.readquotes

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.LiveData
import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class QuotesViewModel : ViewModel(){
    private val QUOTE_URL = "http://api.forismatic.com/api/1.0/"

    private var quote: MutableLiveData<String>? = null

    fun getQuotes(b: Boolean): LiveData<String> {
        if(!b){
            quote = null
        }
        if (quote == null) {
            quote = MutableLiveData()
            loadQuote()
        }
        return quote as MutableLiveData<String>
    }

    private fun loadQuote() {
        val client = AsyncHttpClient()

        client.get(QUOTE_URL, getParams(), object : JsonHttpResponseHandler(){
            @SuppressLint("SetTextI18n")
            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: JSONObject) {
                super.onSuccess(statusCode, headers, response)
                val JsonResponse = response.toString()
                val quoteText = response.getString("quoteText")
                val author = response.getString("quoteAuthor")
                val result = "$quoteText \n\n-$author"
                quote!!.value = result
                Log.i("MainActivity", "onSuccess: $JsonResponse")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, throwable: Throwable, errorResponse: JSONObject) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Log.e("MainActivity", "onFailure: $errorResponse")
            }
        })
    }

    private fun getParams(): RequestParams {
        val params = RequestParams()
        params.put("method","getQuote")
        params.put("format","json")
        params.put("lang","en")
        return params
    }
}