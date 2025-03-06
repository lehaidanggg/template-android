package com.example.template

import com.example.template.data.SharedPrefs
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.AdsApplication
import com.nlbn.ads.util.AppFlyer
import com.nlbn.ads.util.AppOpenManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : AdsApplication() {

    override fun onCreate() {
        super.onCreate()
        injectModule()
        SharedPrefs.init(applicationContext)
        //
        Firebase.crashlytics.isCrashlyticsCollectionEnabled = !BuildConfig.DEBUG
        AppFlyer.getInstance().initAppFlyer(this, getString(R.string.app_flyer), true, false, true)
        Admob.getInstance().setOpenActivityAfterShowInterAds(true)
        AppOpenManager.getInstance()
    }

    override fun enableAdsResume(): Boolean {
        return Admob.getInstance().isLoadFullAds
    }
    override fun getKeyRemoteIntervalShowInterstitial(): String = ""
    override fun getListTestDeviceId(): MutableList<String> = mutableListOf("61AFC09657EC976743A413E0ECC4CD30")
    override fun getResumeAdId(): String = getString(com.nlbn.ads.R.string.ads_test_resume)
    override fun buildDebug(): Boolean = BuildConfig.DEBUG


    private fun injectModule() {
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}