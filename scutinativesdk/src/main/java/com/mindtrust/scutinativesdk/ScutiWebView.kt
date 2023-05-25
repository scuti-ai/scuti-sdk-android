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
import org.json.JSONObject

class ScutiWebView : Fragment()  {

    private var callback: ScutiInterface? = null
    //Private
    //private val BASE_URL = "https://staging.run.app.scuti.store/?gameId=6db28ef4-69b0-421a-9344-31318f898790"
    private var base_url = "https://dev.run.app.scuti.store/?gameId=1e6e003f-0b94-4671-bc35-ccc1b48ce87d&platform=Unity"
    private lateinit var webView:WebView

    private lateinit var targetEnvironment:TargetEnvironment;
    private lateinit var appId:String;

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
                    //webView.evaluateJavascript("initializeApp();", null);
                    getNewProducts()
                    getNewRewards()

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
        //webView.settings.useWideViewPort = true

        webView.addJavascriptInterface(JSBridge(requireContext(), this),"JSBridge");

        Log.d("INFO", "onCreate ScutiWebView callback::"+callback);

        callback?.onWebViewLoadCompleted()

        return view;
    }

    fun  init(environment:TargetEnvironment, id:String) {
        targetEnvironment = environment;
        appId = id;

        base_url = targetEnvironment.type+"?gameId="+appId+"&platform=Unity"
        /*val userToken = getToken();
        Log.d("INFO", "<----0 INIT ScutiWebView 0----> TOKEN:{"+userToken+"}");
        base_url = if (!userToken.isNullOrBlank()) {
            targetEnvironment.type+"?gameId="+appId+"&platform=Unity&userToken="+userToken
        } else {
            targetEnvironment.type+"?gameId="+appId+"&platform=Unity"
        }*/
    }

    fun load(){
        Log.d("INFO", "<----0 LOAD ScutiWebView 0----> ");
        val userToken = getToken();
        Log.d("INFO", "<----8 INIT ScutiWebView 8----> TOKEN:{"+userToken+"}");
        base_url = if (userToken.isNullOrBlank()) {
            targetEnvironment.type+"?gameId="+appId+"&platform=Unity&userToken="+userToken
        } else {
            targetEnvironment.type+"?gameId="+appId+"&platform=Unity"
        }
        Log.d("INFO", "loadUrl: "+base_url);
        webView.loadUrl(base_url)
    }

    fun getNewProducts() {
        Log.d("INFO", "<----0 getNewProducts() 0----> ");
        webView.evaluateJavascript("getNewProducts();", null)
    }

    fun getNewRewards() {
        Log.d("INFO", "<----0 getNewRewards() 0----> ");
        webView.evaluateJavascript("getNewRewards();", null)
    }

    internal fun saveToken(token:String){
        Log.d("INFO", "<----0 Saving User Token {"+token+"} 0----> ");
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            Log.d("INFO", "   <----0 Saving... {"+token+"} 0----> ");
            putString(getString(R.string.user_token), token)
            apply()
        }
    }

    internal fun getToken(): String? {
        Log.d("INFO", "<----0 Getting User Token 0----> ");
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return ""
        Log.d("INFO", "   <----0 Getting... 0----> ");
        return sharedPref.getString(getString(R.string.user_token), "");
    }

    internal fun clearToken() {
        Log.d("INFO", "<----0 Clearing User Token 0----> ");
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        Log.d("INFO", "<----0 Clearing... 0----> ");
        with (sharedPref.edit()) {
            remove(getString(R.string.user_token))
            commit()
        }
    }

    /**
     * Receive message from webview and pass on to native.
     */
    class JSBridge(val context: Context, val view:ScutiWebView){
        @JavascriptInterface
        fun showMessageInNative(message:String){
            Log.d("INFO", "<******0== Message ==0******> "+message);
            val callback = context as ScutiInterface
            val answer = JSONObject(message)
            Log.d("INFO", "<******0== Message Value ==0******> "+answer.get("message"));
            Toast.makeText(context,message, Toast.LENGTH_LONG).show()
            when(answer.get("message") as String){
                ScutiStoreMessages.MSG_BACK_TO_THE_GAME.type -> callback?.onBackToTheGame()
                ScutiStoreMessages.MSG_SCUTI_EXCHANGE.type -> println(message)
                ScutiStoreMessages.MSG_NEW_REWARDS.type -> callback?.onNewRewards(answer.get("payload") as Int > 0)
                ScutiStoreMessages.MSG_NEW_PRODUCTS.type -> callback?.onNewProducts(answer.get("payload") as Int > 0)
                ScutiStoreMessages.MSG_USER_TOKEN.type -> view.saveToken(answer.get("payload") as String)
                ScutiStoreMessages.MSG_STORE_IS_READY.type -> callback?.onStoreIsReady()
                ScutiStoreMessages.MSG_LOG_OUT.type -> view.clearToken()
                else -> println(message)
            }
        }
    }
}