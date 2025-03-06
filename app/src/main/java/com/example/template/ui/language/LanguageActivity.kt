package com.example.template.ui.language

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.template.R
import com.example.template.admob.AdUtils
import com.example.template.base.activity.BaseActivity
import com.example.template.base.viewmodel.safeLaunch
import com.example.template.data.SharedPrefs
import com.example.template.data.models.AppLanguage
import com.example.template.databinding.ActivityLanguageBinding
import com.example.template.ui.home.HomeActivity
import com.example.template.ui.intro.IntroActivity

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(
    ActivityLanguageBinding::inflate
) {
    private lateinit var adapter: LanguageAdapter
    private var languageCode = ""
    private var isFromSplash: Boolean = false
    private var isSelected: Boolean = false

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        setupRCV()
    }

    override fun setupListener() {
        super.setupListener()
        binding.btnDone.setOnClickListener {
            onClickDone()
        }
        //
        isFromSplash = intent.extras?.getBoolean(IS_FROM_SPLASH) ?: false
        if (isFromSplash) {
            AdUtils.displayNativeAdLanguageFirst(
                this,
                getString(R.string.native_language),
                binding.frAd,
            )
        } else {
            preLoadAd()
        }
    }

    private fun setupRCV() {
        adapter = LanguageAdapter(
            AppLanguage.appSupportedLanguages,
            object : IClickLanguage {
                override fun onClickLanguage(language: AppLanguage, position: Int) {
                    languageCode = language.code
                    binding.btnDone.alpha = 1F
                    if (!isSelected) {
                        AdUtils.displayNativeAdLanguageFirst(
                            this@LanguageActivity,
                            getString(R.string.native_language_selected),
                            binding.frAd,
                        )
                    }
                    isSelected = true
                }
            }
        )

        binding.rcv.adapter = adapter
    }


    private fun onClickDone() {
        if (languageCode.isEmpty()) {
            return
        }
        SharedPrefs.languageCode = languageCode
        if (isFromSplash) {
            Intent(this, IntroActivity::class.java).also {
                startActivity(it)
                finish()
            }
        } else {
            Intent(this, HomeActivity::class.java).also {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
                finishAffinity()
            }
        }
    }


    private fun preLoadAd() = lifecycleScope.safeLaunch {
        AdUtils.loadAndDisplayNative(
            applicationContext,
            getString(R.string.native_language),
            binding.frAd,
            true,
            layoutFullAd = R.layout.ads_media_normal
        )

        AdUtils.preloadNativeAd(
            applicationContext,
            getString(R.string.native_language_selected),
            true
        )
    }


    companion object {
        private const val IS_FROM_SPLASH = "is_from_splash"
        const val TAG = "LanguageActivity"

        fun newIntent(context: AppCompatActivity, isFromSplash: Boolean): Intent {
            val intent = Intent(context, LanguageActivity::class.java)
            intent.putExtra(IS_FROM_SPLASH, isFromSplash)
            return intent
        }
    }

}