package com.example.template

import com.example.template.data.SharedPrefs
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.AdsApplication
import com.nlbn.ads.util.AppFlyer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : AdsApplication() {

    override fun onCreate() {
        super.onCreate()
        SharedPrefs.init(applicationContext)
        //
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        AppFlyer.getInstance().initAppFlyer(this, getString(R.string.app_flyer), true, false, true)
        Admob.getInstance().setOpenActivityAfterShowInterAds(true)
    }

    override fun enableAdsResume(): Boolean = BuildConfig.DEBUG
    override fun getKeyRemoteIntervalShowInterstitial(): String = ""
    override fun getListTestDeviceId(): MutableList<String> = mutableListOf("61AFC09657EC976743A413E0ECC4CD30")
    override fun getResumeAdId(): String = getString(R.string.resume_all)
    override fun buildDebug(): Boolean = BuildConfig.DEBUG
}