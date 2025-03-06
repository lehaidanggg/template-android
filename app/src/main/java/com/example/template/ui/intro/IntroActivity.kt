package com.example.template.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.template.R
import com.example.template.admob.AdUtils
import com.example.template.base.activity.BaseActivity
import com.example.template.common.ext.gone
import com.example.template.common.ext.visible
import com.example.template.data.SharedPrefs
import com.example.template.data.models.AdKey
import com.example.template.data.models.IntroModel
import com.example.template.databinding.ActivityIntroBinding
import com.example.template.ui.home.HomeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroActivity : BaseActivity<ActivityIntroBinding>(
    ActivityIntroBinding::inflate
), IIntroAdapterListener {
    private lateinit var introAdapter: IntroAdapter
    private val viewmodel: IntroVM by viewModel()


    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        setupViewPager()
        viewmodel.loadNativeAD(applicationContext)
    }

    override fun observeUI() {
        super.observeUI()
        //
        viewmodel.intros.observe(this) { nativeAds ->
            binding.viewPager.isUserInputEnabled = (nativeAds != null)
            if (nativeAds.isEmpty()) return@observe
            //
            binding.vLoadingAd.gone()
            introAdapter.dataSetChanged(nativeAds)
            binding.viewPager.isUserInputEnabled = true
        }
    }

    private fun setupViewPager() = with(binding) {
        val firstIntros = mutableListOf(IntroModel.defaultIntro().first())
        introAdapter = IntroAdapter(firstIntros, this@IntroActivity)

        viewPager.adapter = introAdapter
        vLoadingAd.visible()
    }

    private fun nextPage() {
        if (binding.viewPager.currentItem < introAdapter.itemCount - 1) {
            binding.viewPager.currentItem += 1
        }
    }

    private fun startHome() {
        val intent = HomeActivity.newIntent(this)
        startActivity(intent)
        SharedPrefs.isFirstInstall = false
    }

    override fun onClickNext(intro: IntroModel) {
        if (intro.keyAD == AdKey.INTRO_4) {
            showInterIntro()
            return
        }
        nextPage()
    }

    private fun showInterIntro() {
        AdUtils.loadAndShowInter(
            this,
            getString(R.string.inter_home),
        ) {
            startHome()
        }
    }


    companion object {
        const val TAG = "IntroActivity"
        const val TIME_DELAY_NEXT_ACTION = 8_000L
        const val TIME_DELAY_SHOW_TIMER = 3_000L


        fun newIntent(context: AppCompatActivity): Intent {
            val intent = Intent(context, IntroActivity::class.java)
            return intent
        }
    }

}