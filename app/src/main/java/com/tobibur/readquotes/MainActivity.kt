package com.tobibur.readquotes

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val QUOTE_URL = "http://api.forismatic.com/api/1.0/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getQuotes()
    }

    private fun getQuotes() {
        val params = RequestParams()
        params.put("method","getQuote")
        params.put("format","json")
        params.put("lang","en")
        letsDoSomeNetworking(params)
    }

    private fun letsDoSomeNetworking(params: RequestParams) {
        val client = AsyncHttpClient()

        client.get(QUOTE_URL, params, object : JsonHttpResponseHandler(){
            @SuppressLint("SetTextI18n")
            override fun onSuccess(statusCode: Int, headers: Array<Header>, response: JSONObject) {
                super.onSuccess(statusCode, headers, response)
                val JsonResponse = response.toString()
                val quoteText = response.getString("quoteText")
                val author = response.getString("quoteAuthor")
                quoteTextView.text = "$quoteText \n\n-$author"
                Log.i("MainActivity", "onSuccess: $JsonResponse")
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, throwable: Throwable, errorResponse: JSONObject) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                //loopjListener.taskCompleted(errorResponse.toString())
                Log.e("MainActivity", "onFailure: $errorResponse")
            }
        });
    }
}
