package com.example.template.ui.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.template.BuildConfig
import com.example.template.R
import com.example.template.base.activity.BaseActivity
import com.example.template.databinding.ActivitySettingBinding
import com.example.template.ui.dialogs.DialogRatingApp
import com.example.template.ui.dialogs.DialogThankYou
import com.example.template.ui.dialogs.OnRateClickListener
import com.example.template.ui.language.LanguageActivity
import com.nlbn.ads.util.AppOpenManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override fun makeBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        //
        with(binding) {
            vRating.icItem.setImageDrawable(
                AppCompatResources.getDrawable(
                    this@SettingActivity,
                    R.drawable.ic_star
                )
            )
            vRating.tvItem.text = getString(R.string.rating)

            vLanguage.icItem.setImageDrawable(
                AppCompatResources.getDrawable(
                    this@SettingActivity,
                    R.drawable.ic_language
                )
            )
            vLanguage.tvItem.text = getString(R.string.language)

            vShare.icItem.setImageDrawable(
                AppCompatResources.getDrawable(
                    this@SettingActivity,
                    R.drawable.ic_share_setting
                )
            )
            vShare.tvItem.text = getString(R.string.share_to_friends)

            vLanguage.vRoot.setOnClickListener {
                val intent = LanguageActivity.newIntent(this@SettingActivity, false)
                navigateTo(intent)
            }
            vShare.vRoot.setOnClickListener {
                shareThisApp()
            }
            //
            val version = BuildConfig.VERSION_NAME
            tvVersion.text = "Ver: $version"
            //
            vRating.vRoot.setOnClickListener { showRatingDialog() }
            btnBack.setOnClickListener { endThisActivity() }
        }
    }

    private fun showRatingDialog() {
        val callback = object : OnRateClickListener {
            override fun onRateLessFourStart() {
                DialogThankYou().show(supportFragmentManager, "")
            }

            override fun onRateClick(isNeedFinish: Boolean) {
                AppOpenManager.getInstance().disableAppResume()
                val dialogThankYou = DialogThankYou()
                dialogThankYou.show(supportFragmentManager, "")

            }
        }
        val rateDialogFragment = DialogRatingApp(callback)
        rateDialogFragment.show(supportFragmentManager, "rate")
    }

    private fun shareThisApp() {
        AppOpenManager.getInstance().disableAppResume()

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Note")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        )
        startActivity(Intent.createChooser(shareIntent, "Share"))
    }

    companion object {
        fun newIntent(context: AppCompatActivity): Intent {
            val intent = Intent(context, SettingActivity::class.java)
            return intent
        }
    }
}
