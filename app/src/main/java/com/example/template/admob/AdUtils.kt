package com.example.template.admob

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.template.R
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.banner.BannerPlugin
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.callback.BannerCallBack
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.ConsentHelper

object AdUtils {
    private val nativePreloadIntro = mutableMapOf<String, NativeAd?>()

    fun preloadNativeAd(
        context: Context,
        adUnitID: String,
        enabled: Boolean
    ) {
        val callback = object : NativeCallback() {
            override fun onNativeAdLoaded(nativeAd: NativeAd?) {
                super.onNativeAdLoaded(nativeAd)
                nativePreloadIntro[adUnitID] = nativeAd
            }

            override fun onAdFailedToLoad() {
                super.onAdFailedToLoad()
                nativePreloadIntro[adUnitID] = null
            }
        }

        if (Admob.getInstance().isLoadFullAds) {
            Admob.getInstance().loadNativeAd(context, adUnitID, callback)
            return
        }

        if (!enabled) {
            return
        }

        Admob.getInstance().loadNativeAd(context, adUnitID, callback)
    }

    fun displayNativeAd(
        context: Context,
        adUnitID: String,
        parentView: ViewGroup
    ) {
        val adNative = nativePreloadIntro[adUnitID]
        if (adNative == null) {
            parentView.removeAllViews()
            return
        }

        val idLayoutAD = if (Admob.getInstance().isLoadFullAds) {
            R.layout.ads_media_fullad
        } else {
            R.layout.ads_media_normal
        }


        val adView = LayoutInflater.from(context).inflate(idLayoutAD, null) as NativeAdView

        parentView.removeAllViews()
        parentView.addView(adView)
        Admob.getInstance().pushAdsToViewCustom(adNative, adView)
    }

    fun displayNativeAdLanguageFirst(
        context: Context,
        adUnitID: String,
        parentView: ViewGroup
    ) {
        val adNative = nativePreloadIntro[adUnitID]
        if (adNative == null) {
            parentView.removeAllViews()
            return
        }

        val idLayoutAD = R.layout.ads_media_normal
        val adView = LayoutInflater.from(context).inflate(idLayoutAD, null) as NativeAdView

        parentView.removeAllViews()
        parentView.addView(adView)
        Admob.getInstance().pushAdsToViewCustom(adNative, adView)
    }

    fun loadAndDisplayNative(
        context: Context,
        adUnitID: String,
        parentView: ViewGroup,
        enabled: Boolean = Admob.getInstance().isLoadFullAds,
        layoutNormal: Int = R.layout.ads_media_normal,
        layoutFullAd: Int = R.layout.ads_media_fullad,
        onAdShowed: (() -> Unit)? = null
    ) {
        val callback = object : NativeCallback() {
            override fun onNativeAdLoaded(nativeAd: NativeAd) {
                val idLayoutAD =
                    if (Admob.getInstance().isLoadFullAds) layoutFullAd else layoutNormal
                val adView = LayoutInflater.from(context).inflate(idLayoutAD, null) as NativeAdView

                parentView.removeAllViews()
                parentView.addView(adView)
                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView)
                onAdShowed?.invoke()
            }

            override fun onAdFailedToLoad() {
                parentView.removeAllViews()
                parentView.visibility = View.GONE
                onAdShowed?.invoke()
            }
        }

        if (Admob.getInstance().isLoadFullAds) {
            Admob.getInstance().loadNativeAd(context, adUnitID, callback)
            return
        }

        if (!enabled) {
            parentView.removeAllViews()
            parentView.visibility = View.GONE
            onAdShowed?.invoke()
            return
        }

        Admob.getInstance().loadNativeAd(context, adUnitID, callback)
    }

    // =============================================================================================
    fun loadBannerCollapse(
        context: AppCompatActivity,
        adUnitID: String,
        enabled: Boolean = Admob.getInstance().isLoadFullAds,
        parentView: ViewGroup,
        vLoading: ViewGroup,
        timeReload: Int = 30
    ) {
        if (!enabled) {
            parentView.visibility = View.GONE
            return
        }

        parentView.visibility = View.VISIBLE
        val config = BannerPlugin.Config().apply {
            defaultAdUnitId = adUnitID
            defaultBannerType = BannerPlugin.BannerType.CollapsibleBottom
            defaultRefreshRateSec = timeReload
            defaultCBFetchIntervalSec = timeReload
        }
        Admob.getInstance().loadBannerPlugin(context, parentView, vLoading, config)
    }

    fun loadBannerNormal(
        context: AppCompatActivity,
        adUnitID: String,
        parentView: ViewGroup,
        vLoading: ViewGroup,
        enabled: Boolean = Admob.getInstance().isLoadFullAds,
        timeReload: Int = 30,
    ) {
        if (!enabled) {
            parentView.visibility = View.GONE
            return
        }

        parentView.visibility = View.VISIBLE
        val config = BannerPlugin.Config().apply {
            defaultAdUnitId = adUnitID
            defaultBannerType = BannerPlugin.BannerType.Adaptive
            defaultRefreshRateSec = timeReload
            defaultCBFetchIntervalSec = timeReload
        }
        Admob.getInstance().loadBannerPlugin(context, parentView, vLoading, config)
    }

    fun loadBanner(
        context: AppCompatActivity,
        adUnitID: String
    ) {
        val callback = object : BannerCallBack() {

        }

        Admob.getInstance().loadBanner(
            context,
            adUnitID,
            callback
        )
    }


    var mInterIntro: InterstitialAd? = null
    fun preLoadAdsInterView(context: Context, adUnitID: String, enabled: Boolean) {
        val callback = object : AdCallback() {
            override fun onInterstitialLoad(interstitialAd: InterstitialAd?) {
                super.onInterstitialLoad(interstitialAd)
                mInterIntro = interstitialAd
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                mInterIntro = null
            }
        }

        if (Admob.getInstance().isLoadFullAds) {
            Admob.getInstance().loadInterAds(context, adUnitID, callback)
            return
        }

        if (!enabled) {
            mInterIntro = null
            return
        }

        Admob.getInstance().loadInterAds(context, adUnitID, callback)
    }

    fun loadAndShowInter(
        context: AppCompatActivity,
        adUnitID: String,
        enabled: Boolean = Admob.getInstance().isLoadFullAds,
        onNextAction: () -> Unit
    ) {
        if (!ConsentHelper.getInstance(context).canRequestAds()) {
            onNextAction.invoke()
            return
        }

        if (!Admob.getInstance().isLoadFullAds || !enabled) {
            onNextAction.invoke()
            return
        }

        val callback = object : AdCallback() {
            override fun onNextAction() {
                super.onNextAction()
                onNextAction.invoke()
            }

            override fun onAdFailedToLoad(p0: LoadAdError?) {
                super.onAdFailedToLoad(p0)
                onNextAction.invoke()
            }
        }
        Admob.getInstance().loadAndShowInter(context, adUnitID, 100, 100, callback)
    }
}
