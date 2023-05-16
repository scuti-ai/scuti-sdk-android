package com.MindTrust.scuti_wrapper

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mindtrust.scutinativesdk.ScutiButton
import com.mindtrust.scutinativesdk.ScutiInterface
import com.mindtrust.scutinativesdk.ScutiWebView


class MainActivity : AppCompatActivity(), ScutiInterface {

    //Private
    //private val BASE_URL = "https://staging.run.app.scuti.store/?gameId=6db28ef4-69b0-421a-9344-31318f898790&platform=Unity"
    private val BASE_URL = "https://dev.run.app.scuti.store/?gameId=1e6e003f-0b94-4671-bc35-ccc1b48ce87d&platform=Unity"
    private lateinit var webViewLayout: FrameLayout

    private lateinit var manager: FragmentManager
    private lateinit var scutiButton: ScutiButton
    private lateinit var scutiWebView: ScutiWebView
    private lateinit var transaction: FragmentTransaction


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("INFO", "-*-*-*-*-* INIT *-*-*-*-*-");

        webViewLayout = findViewById(R.id.fragment_webview)

        manager = supportFragmentManager
        scutiButton = ScutiButton()
        transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_scutibtn, scutiButton)
        transaction.commit()

        scutiWebView = ScutiWebView()
        transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_webview, scutiWebView)
        transaction.commit()

    }

    private fun onJsEvalCallback (): ValueCallback<String>? {
        Log.d("INFO", "<******0 onJsEvalCallback 0******> ");
        return null
    }

    override fun onBackPressed() {
        /*val webView  = findViewById<WebView>(R.id.webView)
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }*/
    }

    override fun onWebViewLoadCompleted() {
        Log.d("INFO", "*-*-*-*-*-*-* OOOO*1*OOOO *-*-*-*-*-*-*");
        scutiWebView.load(BASE_URL)
    }

    override fun onButtonLoadCompleted() {
        Log.d("INFO", "*-*-*-*-*-*-* OOOO*2*OOOO *-*-*-*-*-*-*");
        scutiButton.showNewItemImage(false)
        scutiButton.showNotificationIcon(false)
    }

    override fun onScutiButtonClicked() {
        Log.d("INFO", "*-*-*-*-*-*-* OOOO*3*OOOO *-*-*-*-*-*-*");
        webViewLayout.visibility = View.VISIBLE
    }
}