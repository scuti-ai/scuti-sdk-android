package com.MindTrust.scuti_wrapper

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mindtrust.scutinativesdk.ScutiButton


class MainActivity : AppCompatActivity() {

    //Private
    //private val BASE_URL = "https://staging.run.app.scuti.store/?gameId=6db28ef4-69b0-421a-9344-31318f898790&platform=Unity"
    private val BASE_URL = "https://dev.run.app.scuti.store/?gameId=1e6e003f-0b94-4671-bc35-ccc1b48ce87d&platform=Unity"
    //private var initialized = false;
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("INFO", "-*-*-*-*-* INIT *-*-*-*-*-");

        var scutiBtn  = findViewById<ScutiButton>(R.id.myScutiBtn);

        //scutiBtn.showNotificationIcon(false);

        /*var scutiBtn  = findViewById<ImageButton>(R.id.myScutiBtn);
        scutiB.setOnClickListener{
            Log.d("INFO", "-*-*-*-*-* CLICKED *-*-*-*-*-");
            Toast.makeText(applicationContext,"Scuti button clicked!!", Toast.LENGTH_LONG).show()
        }*/

/*
        //Webview
        val webView  = findViewById<WebView>(R.id.webView)
        webView.webChromeClient = object : WebChromeClient(){
            fun onPageFinished(view: WebView, url: String) {
                Log.d("INFO", "<******* Page Loaded *******>");
            }
        }

        webView.webViewClient = object : WebViewClient(){
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

                    //val bridgeMessage = WebMessage("function sendMessage(msg) {\n" +
                    //        "console.log('testing code inject');\n" +
                    //        "var valueReceived =  msg;\n" +
                    //        "JSBridge.showMessageInNative(valueReceived);\n" +
                    //        "console.log(valueReceived);\n" +
                    //        "}");
                    //webView.postWebMessage(bridgeMessage, Uri.parse(url));
                    webView.evaluateJavascript(
                        "function sendMessage(msg) {\n" +
                                "console.log('testing code inject');\n" +
                                "var valueReceived =  msg;\n" +
                                "JSBridge.showMessageInNative(valueReceived);\n" +
                                "}", onJsEvalCallback()
                    );

                    //val testMessage = WebMessage("sendMessage('this a test string');");
                    //webView.postWebMessage(testMessage, Uri.parse(url));
                    webView.evaluateJavascript(
                        "sendMessage('this a test string');",
                        onJsEvalCallback()
                    );


                    /*if (Looper.getMainLooper().isCurrentThread) {
                    Log.d("INFO", "<8===- On UI thread -===8> ");
                    // On UI thread.
                } else {
                    // Not on UI thread.
                    Log.d("INFO", "<8===- NOT On UI thread -===8> ");
                }
                if (Looper.getMainLooper().thread == Thread.currentThread()) {
                    Log.d("INFO", "<8--===- On UI thread -===--8> ");
                    // On UI thread.
                } else {
                    // Not on UI thread.
                    Log.d("INFO", "<8--===- NOT On UI thread -===--8> ");
                }*/

                    //val getNewProductsMessage = WebMessage("getNewProducts();");
                    //webView.postWebMessage(getNewProductsMessage, Uri.parse(url));
                    //val getNewRewardsMessage = WebMessage("getNewRewards();");
                    //webView.postWebMessage(getNewRewardsMessage, Uri.parse(url));
                    //webView.evaluateJavascript("getNewProducts();", null);
                    //webView.evaluateJavascript("getNewProducts();", null);
                    Log.d("INFO", "<===- 0 starting Delay 0 -===> ");
                    /*Handler().postDelayed({
                    Log.d("INFO", "<===- 0 Calling function 0 -===> ");
                    webView.loadUrl("javascript:getNewProducts()");
                }, 10000)*/

                    // try to call method from UI thread
                    /*this@MainActivity.runOnUiThread(java.lang.Runnable {
                    if (Looper.getMainLooper().isCurrentThread) {
                        Log.d("INFO", "<===- On UI thread -===> ");
                        // On UI thread.
                    } else {
                        // Not on UI thread.
                        Log.d("INFO", "<===- NOT On UI thread -===> ");
                    }
                    Log.d("INFO", "<===- 0 Calling function 0 -===> ");

                    if (Looper.getMainLooper().thread == Thread.currentThread()) {
                        Log.d("INFO", "<--===- On UI thread -===--> ");
                        // On UI thread.
                    } else {
                        // Not on UI thread.
                        Log.d("INFO", "<--===- NOT On UI thread -===--> ");
                    }
                    //webView.evaluateJavascript("getNewProducts();", null);
                    webView.loadUrl("javascript:getNewProducts()");
                })*/
                    //Log.d("INFO", "<===- initialized -===> " + initialized);
                    //if(!initialized)
                    //{
                    //initialized = true;
                    webView.evaluateJavascript("initializeApp();", null);
                    //}
                    //webView.loadUrl("javascript:getNewProducts()");
                    /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("getNewProducts();", null/*onJsEvalCallback()*/);
                } else {
                    webView.loadUrl("javascript:getNewProducts();");
                }*/
                }
            }
        }
        webView.settings.domStorageEnabled = true;
        //webView.settings.databaseEnabled = true;

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JSBridge(this),"JSBridge");



        Log.d("INFO", "onCreate MainActivity");
        //val settings : WebSettings = webView.settings
        //settings.javaScriptEnabled = true



        Log.d("INFO", "loadUrl: "+BASE_URL);
        webView.loadUrl(BASE_URL)

        //webView.evaluateJavascript(/*Sample*/"", onJsEvalCallback())
*/
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
}