package com.mindtrust.scutinativesdk

interface ScutiInterface {
    fun onWebViewLoadCompleted()
    fun onButtonLoadCompleted()
    fun onScutiButtonClicked()
    fun onBackToTheGame()
    fun onNewProducts(show: Boolean)
    fun onNewRewards(show: Boolean)
    fun onStoreIsReady()
}