package com.mindtrust.scutinativesdk

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebMessage
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout

class ScutiWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle:Int = 0,
    defStyleRes:Int = 0
) : ConstraintLayout(context, attrs, defStyle, defStyleRes) {

    //Private
    //private val BASE_URL = "https://staging.run.app.scuti.store/?gameId=6db28ef4-69b0-421a-9344-31318f898790"
    private val BASE_URL = "https://dev.run.app.scuti.store/?gameId=1e6e003f-0b94-4671-bc35-ccc1b48ce87d&platform=Unity"

    init{
        LayoutInflater.from(context).inflate(R.layout.scutiwebview, this, true)

        //Webview
        /*val webView  = findViewById<WebView>(R.id.webView)
        webView.webChromeClient = object : WebChromeClient(){
            fun onPageFinished(view: WebView, url: String) {
                Log.d("INFO", "<******* Page Loaded *******>");
            }
        }

        webView.webViewClient = object : WebViewClient(){
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPageFinished(view: WebView, url: String) {
                Log.d("INFO", "<******0 Page Loaded 0******> "+url);

                if (url.startsWith("unity:")) {
                    val message = url.substring(6);
                    Log.d("INFO", "<******0 unity: 0******> "+message);

                } else {

                    webView.evaluateJavascript(
                        "if (window && window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.unityControl) {\n" +
                                "     console.log(' ==> IF ---> ');\n" +
                                "     window.Unity = {\n" +
                                "         call: function(msg) {\n" +
                                "             console.log('call1: '+msg);\n" +
                                "             window.webkit.messageHandlers.unityControl.postMessage(msg);\n" +
                                "         }\n" +
                                "     }\n" +
                                " } else {\n" +
                                "     console.log(' ==> ELSE ---> '+window.webkit);\n" +
                                "     window.Unity = {\n" +
                                "         call: function(msg) {\n" +
                                "             console.log('call2: '+msg);\n" +
                                "             JSBridge.showMessageInNative(msg);//window.location = 'unity:' + msg;\n" +
                                "         }\n" +
                                "     }\n" +
                                " }", null
                    );
                    webView.evaluateJavascript("initializeApp();", null);

                }
                /*val getNewProductsMessage = WebMessage("getNewProducts();");
                webView.postWebMessage(getNewProductsMessage, Uri.parse(url));
                val getNewRewardsMessage = WebMessage("getNewRewards();");
                webView.postWebMessage(getNewRewardsMessage, Uri.parse(url));*/
                /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("getNewProducts();", null/*onJsEvalCallback()*/);
                } else {
                    webView.loadUrl("javascript:getNewProducts();");
                }*/
            }
        }
        webView.settings.domStorageEnabled = true;
        //webView.settings.databaseEnabled = true;

        webView.settings.javaScriptEnabled = true

        webView.addJavascriptInterface(JSBridge(context),"JSBridge");

        Log.d("INFO", "onCreate MainActivity");
        //val settings : WebSettings = webView.settings
        //settings.javaScriptEnabled = true

        /*Log.d("INFO", "loadUrl: "+BASE_URL);
        webView.loadUrl(BASE_URL)*/
        Log.d("INFO", "loadUrl: "+BASE_URL);*/
    }

    /**
     * Receive message from webview and pass on to native.
     */
    class JSBridge(val context: Context){
        @JavascriptInterface
        fun showMessageInNative(message:String){
            Log.d("INFO", "<******0 Message 0******> "+message);
            Toast.makeText(context,message, Toast.LENGTH_LONG).show()
        }
    }
}