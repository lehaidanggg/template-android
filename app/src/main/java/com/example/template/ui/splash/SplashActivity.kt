package com.example.template.ui.splash

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.example.template.BuildConfig
import com.example.template.R
import com.example.template.admob.AdUtils
import com.example.template.base.activity.BaseActivity
import com.example.template.data.FBConfig
import com.example.template.data.SharedPrefs
import com.example.template.databinding.ActivitySpashBinding
import com.example.template.ui.dialogs.DialogForceUpdate
import com.example.template.ui.dialogs.IClickForeUpdate
import com.example.template.ui.intro.IntroActivity
import com.example.template.ui.language.LanguageActivity
import com.nlbn.ads.callback.AdCallback
import com.nlbn.ads.util.Admob
import com.nlbn.ads.util.AppOpenManager
import com.nlbn.ads.util.ConsentHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySpashBinding>(
    ActivitySpashBinding::inflate
) {
    private var forceUpdateDialog: DialogForceUpdate? = null
    private val backgroundScope = CoroutineScope(Dispatchers.IO)


    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        checkUpdate()
    }

    private fun checkUpdate() {
        if (FBConfig.isForceUpdate.get()) {
            showDialogForceUpdate(
                onClickUpdate = {
                    runOnUiThread {
                        onClickUpdateDialog()
                    }
                },
                onClickNoThanks = {
                    runOnUiThread {
                        onClickNoThankDialog()
                    }
                })
            return
        }
        initAd()
    }

    private fun initAd() {
        Admob.getInstance().setOpenActivityAfterShowInterAds(true)
        val consentHelper = ConsentHelper.getInstance(this)
        if (!consentHelper.canLoadAndShowAds()) {
            consentHelper.reset()
        }

        consentHelper.obtainConsentAndShow(this) {
            preloadNextNative()
            loadAndShowInter()
//            lifecycleScope.safeLaunch {
//                delay(2000)
//                loadAndShowOpenAD()
//            }
        }
    }

    private fun preloadNextNative() = backgroundScope.launch {
        if (SharedPrefs.isFirstInstall) {
            AdUtils.preloadNativeAd(
                applicationContext,
                getString(R.string.native_language),
                true
            )
            AdUtils.preloadNativeAd(
                applicationContext,
                getString(R.string.native_language_selected),
                true
            )
        }
    }

    private fun startAct() {
        if (SharedPrefs.isFirstInstall) {
            val intent = LanguageActivity.newIntent(this, true)
            startActivity(intent)
            finish()
        } else {
            val intent = IntroActivity.newIntent(this)
            startActivity(intent)
            finish()
        }
    }

    private fun onClickUpdateDialog() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        }
        finish()
    }

    private fun onClickNoThankDialog() {
        dismissDialogForceUpdate()
        initAd()
    }

    private fun showDialogForceUpdate(
        onClickUpdate: () -> Unit,
        onClickNoThanks: () -> Unit
    ) {
        dismissDialogForceUpdate()
        forceUpdateDialog = DialogForceUpdate(
            onlyUpdate = true,
            object :
                IClickForeUpdate {
                override fun onClickUpdate() {
                    onClickUpdate.invoke()
                }

                override fun onClickNoThanks() {
                    onClickNoThanks.invoke()
                }
            }
        ).apply {
            show(supportFragmentManager, tag)
        }
    }

    private fun dismissDialogForceUpdate() {
        forceUpdateDialog?.dismiss()
    }

    // =================================================================
    private fun loadAndShowInter() {
        val callBack = object : AdCallback() {
            override fun onNextAction() {
                super.onNextAction()
                startAct()
            }
        }

        Admob.getInstance().loadSplashInterAds2(
            this@SplashActivity,
            getString(R.string.inter_splash),
            2000,
            callBack
        )
    }

    private fun loadAndShowOpenAD() {
        if (!Admob.getInstance().isLoadFullAds) {
            startAct()
            return
        }

        val callBack = object : AdCallback() {
            override fun onAdClosed() {
                super.onAdClosed()
                startAct()
            }
        }

        AppOpenManager.getInstance().loadOpenAppAdSplash(
            this,
            getString(R.string.resume_all),
            100,
            100,
            Admob.getInstance().isLoadFullAds,
            callBack
        )
    }

    // =================================================================
    override fun onStop() {
        super.onStop()
        Admob.getInstance().dismissLoadingDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        Admob.getInstance().dismissLoadingDialog()
        dismissDialogForceUpdate()
        backgroundScope.cancel()
    }

}