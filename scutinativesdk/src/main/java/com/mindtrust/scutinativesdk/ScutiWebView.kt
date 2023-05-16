package com.mindtrust.scutinativesdk

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment


interface ScutiInterface {
    fun onWebViewLoadCompleted()
    fun onButtonLoadCompleted()
    fun onScutiButtonClicked()
}


class ScutiWebView : Fragment()  {

    private var callback: ScutiInterface? = null
    //Private
    //private val BASE_URL = "https://staging.run.app.scuti.store/?gameId=6db28ef4-69b0-421a-9344-31318f898790"
    private var base_url = "https://dev.run.app.scuti.store/?gameId=1e6e003f-0b94-4671-bc35-ccc1b48ce87d&platform=Unity"
    private lateinit var webView:WebView

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.scutiwebview, container, false)

        webView  = view.findViewById(R.id.myWebView)

        Log.d("INFO", "<*****<<--0 onCreateView 0-->>*****>");
        Log.d("INFO", "context::"+context);

        if (context is ScutiInterface) {
            Log.d("INFO", "ScutiInterface::"+context);
            callback = context as ScutiInterface
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

        webView.addJavascriptInterface(JSBridge(requireContext()),"JSBridge");

        Log.d("INFO", "onCreate ScutiWebView callback::"+callback);

        callback?.onWebViewLoadCompleted()

        return view;
    }

    fun load(baseurl:String=""){
        if(baseurl != "") base_url = baseurl
        Log.d("INFO", "<----0 LOAD ScutiWebView 0----> ");

        Log.d("INFO", "loadUrl: "+base_url);
        webView.loadUrl(base_url)
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