package com.tobibur.readquotes

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.widget.TextView
import android.support.design.widget.Snackbar


class MainActivity : AppCompatActivity() ,ConnectivityReceiver.ConnectivityReceiverListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkConnection()
        swipe_refresh_layout.setOnRefreshListener {
            checkConnection()
        }
    }

    private fun readQuotes(b: Boolean) {

        val model = ViewModelProviders.of(this).get(QuotesViewModel::class.java)
        model.getQuotes(b).observe(this, Observer<String> {
            quoteTextView.text = it
            swipe_refresh_layout.isRefreshing = false
        })
    }

    // Method to manually check connection status
    private fun checkConnection() {
        val isConnected = ConnectivityReceiver.isConnected()
        showSnack(isConnected)
    }

    // Showing the status in Snackbar
    private fun showSnack(isConnected: Boolean) {

        val message: String
        val color: Int
        if (isConnected) {
            readQuotes(false)
        } else {
            swipe_refresh_layout.isRefreshing = false
            message = "Sorry! Not connected to internet"
            color = Color.WHITE
            val snackbar = Snackbar
                    .make(quoteTextView,message, Snackbar.LENGTH_LONG)

            val sbView = snackbar.view
            val textView = sbView.findViewById(android.support.design.R.id.snackbar_text) as TextView
            textView.setTextColor(color)
            snackbar.show()
        }
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if(!isConnected){
            showSnack(isConnected)
        }
    }

}
