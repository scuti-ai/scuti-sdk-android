package com.mindtrust.scutinativesdk

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
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

    private lateinit var targetEnvironment:TargetEnvironment
    private lateinit var _logSettings: LogSettings
    var logSettings: LogSettings = LogSettings.ERROR_ONLY
        get() = _logSettings

    private lateinit var appId:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.scutiwebview, container, false)

        webView  = view.findViewById(R.id.myWebView)

        if (context is ScutiInterface) {
            callback = context as ScutiInterface
        }

        webView.webViewClient = object : WebViewClient(){
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPageFinished(view: WebView, url: String) {

                if (url.startsWith("unity:")) {
                    val message = url.substring(6);
                    ScutiLogger.getInstance().log(" unity: $message")
                } else {

                    webView.evaluateJavascript(
                        "if (window && window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.unityControl) {\n" +
                                "     window.Unity = {\n" +
                                "         call: function(msg) {\n" +
                                "             window.webkit.messageHandlers.unityControl.postMessage(msg);\n" +
                                "         }\n" +
                                "     }\n" +
                                " } else {\n" +
                                "     window.Unity = {\n" +
                                "         call: function(msg) {\n" +
                                "             JSBridge.showMessageInNative(msg);//window.location = 'unity:' + msg;\n" +
                                "         }\n" +
                                "     }\n" +
                                " }", null
                    )
                    //webView.evaluateJavascript("initializeApp();", null);
                    getNewProducts()
                    getNewRewards()

                }

            }

        }

        webView.webChromeClient = object : WebChromeClient() {

            override fun onConsoleMessage(message: ConsoleMessage): Boolean {
                when(message.messageLevel()){
                    ConsoleMessage.MessageLevel.ERROR -> ScutiLogger.getInstance().logError("${message.message()} -- From line ${message.lineNumber()} of ${message.sourceId()}")
                    ConsoleMessage.MessageLevel.LOG -> {
                        if(message.message().lowercase().contains("error"))
                            ScutiLogger.getInstance().logError("${message.message()} -- From line ${message.lineNumber()} of ${message.sourceId()}")
                        else
                            ScutiLogger.getInstance().log("${message.message()} -- From line ${message.lineNumber()} of ${message.sourceId()}")
                    }
                    else -> ScutiLogger.getInstance().log("${message.message()} -- From line ${message.lineNumber()} of ${message.sourceId()}")
                }
                return true
            }
        }

        webView.settings.domStorageEnabled = true
        //webView.settings.databaseEnabled = true;

        webView.settings.javaScriptEnabled = true
        //webView.settings.useWideViewPort = true

        webView.addJavascriptInterface(JSBridge(requireContext(), this),"JSBridge")

        callback?.onWebViewLoadCompleted()

        return view
    }

    fun  init(environment:TargetEnvironment, id:String, logSettings: LogSettings) {
        targetEnvironment = environment
        _logSettings = logSettings
        ScutiLogger.getInstance().setLogSettings(_logSettings)
        appId = id
        base_url = targetEnvironment.type+"?gameId="+appId+"&platform=Unity"
    }

    fun load(){
        val userToken = getToken()
        base_url = if (userToken.isNullOrBlank()) {
            targetEnvironment.type+"?gameId="+appId+"&platform=Unity&userToken="+userToken
        } else {
            targetEnvironment.type+"?gameId="+appId+"&platform=Unity"
        }
        webView.loadUrl(base_url)
    }

    fun getNewProducts() {
        webView.evaluateJavascript("getNewProducts();", null)
    }

    fun getNewRewards() {
        webView.evaluateJavascript("getNewRewards();", null)
    }

    fun setUserId(userId:String) {
        webView.evaluateJavascript("setGameUserId(\"$userId\");", null)
    }

    internal fun saveToken(token:String){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(getString(R.string.user_token), token)
            apply()
        }
    }

    internal fun getToken(): String? {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return ""
        return sharedPref.getString(getString(R.string.user_token), "")
    }

    internal fun clearToken() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
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
            val callback = context as ScutiInterface
            val answer = JSONObject(message)
            ScutiLogger.getInstance().log("From HTML: $message")
            when(answer.get("message") as String){
                ScutiStoreMessages.MSG_BACK_TO_THE_GAME.type -> callback?.onBackToTheGame()
                ScutiStoreMessages.MSG_SCUTI_EXCHANGE.type -> callback?.onScutiExchange((answer.get("payload") as Int).toString())
                ScutiStoreMessages.MSG_NEW_REWARDS.type -> callback?.onNewRewards(answer.get("payload") as Int > 0)
                ScutiStoreMessages.MSG_NEW_PRODUCTS.type -> callback?.onNewProducts(answer.get("payload") as Int > 0)
                ScutiStoreMessages.MSG_USER_TOKEN.type -> view.saveToken(answer.get("payload") as String)
                ScutiStoreMessages.MSG_STORE_IS_READY.type -> callback?.onStoreIsReady()
                ScutiStoreMessages.MSG_LOG_OUT.type -> view.clearToken()
                else -> ScutiLogger.getInstance().log("No Scuti Message: $message")

            }
        }
    }
}