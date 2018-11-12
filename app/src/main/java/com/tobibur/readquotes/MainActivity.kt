package com.tobibur.readquotes

import androidx.lifecycle.Observer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.ViewModelProviders
import android.graphics.Color
import com.google.android.material.snackbar.Snackbar
import android.util.Log
import android.widget.TextView
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity(){

    private var connectivityDisposable: Disposable? = null
    private var internetDisposable: Disposable? = null

    companion object {
        private const val TAG = "ReactiveNetwork"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    override fun onResume() {
        super.onResume()

        connectivityDisposable = ReactiveNetwork.observeNetworkConnectivity(applicationContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { connectivity ->
                    Log.d(TAG, connectivity.toString())
                    //val state = connectivity.state()
                    //val name = connectivity.typeName()
                    //val string = String.format("state: %s, typeName: %s", state, name)
                    //Toast.makeText(this, string,Toast.LENGTH_SHORT).show()
                }

        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isConnectedToInternet ->
                    //internet_status.text = isConnectedToInternet.toString()
                    showSnack(isConnectedToInternet)
                    //checkInternet = isConnectedToInternet
                    swipe_refresh_layout.setOnRefreshListener {
                        if(isConnectedToInternet){
                            readQuotes(true)
                        }else{
                            showSnack(isConnectedToInternet)
                        }
                    }
                }
    }

    override fun onPause() {
        super.onPause()
        safelyDispose(connectivityDisposable)
        safelyDispose(internetDisposable)
    }

    private fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }
    private fun readQuotes(b: Boolean) {

        val model = ViewModelProviders.of(this).get(QuotesViewModel::class.java)
        model.getQuoteData(b).observe(this, Observer<ApiResponse> {
            quoteTextView.text = it!!.posts
            swipe_refresh_layout.isRefreshing = false
        })
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
            val textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(color)
            snackbar.show()
        }
    }

}