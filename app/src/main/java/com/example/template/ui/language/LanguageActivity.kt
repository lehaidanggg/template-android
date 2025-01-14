package com.example.template.ui.language

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {

    private lateinit var adapter: LanguageAdapter
    private var languageCode = ""
    private var isFromSplash: Boolean = false
    private var isSelected: Boolean = false


    override fun makeBinding(layoutInflater: LayoutInflater): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)

        isFromSplash = intent.extras?.getBoolean(IS_FROM_SPLASH) ?: false
        with(binding) {
            btnDone.alpha = 0.5F
            btnDone.setOnClickListener {
                onClickDone()
            }
        }
        //
        adapter = LanguageAdapter(
            AppLanguage.appSupportedLanguages,
            object : IClickLanguage {
                override fun onClickLanguage(language: AppLanguage, position: Int) {
                    languageCode = language.code
                    binding.btnDone.alpha = 1F
                    if (!isSelected) {
                        displayAd(getString(R.string.native_language_selected))
                    }
                    isSelected = true
                }
            }
        )

        binding.rcv.adapter = adapter
        //
        if (isFromSplash) {
            displayAd(getString(R.string.native_language))
        } else {
            preLoadAd()
        }
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

    private fun displayAd(adID: String) {
        AdUtils.displayNativeAd(
            this,
            adID,
            binding.frAd,
            isFromLanguage = true
        )
    }

    private fun preLoadAd() = lifecycleScope.safeLaunch {
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