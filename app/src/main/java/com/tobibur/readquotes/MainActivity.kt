package com.tobibur.readquotes

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.arch.lifecycle.ViewModelProviders



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readQuotes(true)
        swipe_refresh_layout.setOnRefreshListener {
            readQuotes(false)
        }
    }

    private fun readQuotes(b: Boolean) {
        val model = ViewModelProviders.of(this).get(QuotesViewModel::class.java)
        model.getQuotes(b).observe(this, Observer<String> {
            quoteTextView.text = it
            swipe_refresh_layout.isRefreshing = false
        })
    }

}
