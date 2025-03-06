package com.example.template.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.template.base.activity.BaseActivity
import com.example.template.base.viewmodel.safeLaunch
import com.example.template.databinding.ActivityHomeBinding
import com.example.template.ui.setting.SettingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity<ActivityHomeBinding>(
    ActivityHomeBinding::inflate
) {
    private val viewmodel: HomeVM by viewModel()

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        with(binding) {
            lifecycleOwner = this@HomeActivity
            vm = viewmodel
            executePendingBindings()
        }
    }

    override fun observeUI() {
        super.observeUI()
        lifecycleScope.safeLaunch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.onEvent.collect { handleEvent(it) }
            }
        }
    }

    private fun handleEvent(event: HomeVM.Event) {
        when (event) {
            is HomeVM.Event.OpenSetting -> {
                val intent = SettingActivity.newIntent(this)
                startActivity(intent)
            }

            is HomeVM.Event.OpenDialogResult -> {}
        }
    }


    companion object {
        fun newIntent(context: AppCompatActivity): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            return intent
        }
    }

}