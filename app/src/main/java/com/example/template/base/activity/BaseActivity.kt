package com.example.template.base.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.template.R
import com.example.template.base.CoroutineLauncher
import com.example.template.base.dialog.LoadingDialog
import com.example.template.data.SharedPrefs
import kotlinx.coroutines.delay
import java.util.Locale

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {

    private val activityScope: CoroutineLauncher by lazy {
        return@lazy CoroutineLauncher()
    }

    // ============================= OPEN FUNC ====================================
    open val binding: VB by lazy { makeBinding(layoutInflater) }

    abstract fun makeBinding(layoutInflater: LayoutInflater): VB

    open fun setupView(savedInstanceState: Bundle?) {}

    open fun observeUI() {}

    open fun navigateTo(intent: Intent) {
        startActivity(intent)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    open fun <T : AppCompatActivity> navigateTo(
        screen: Class<T>,
        bundle: Bundle? = null,
        isFinish: Boolean = false,
        flags: Int? = null
    ) {
        val intent = Intent(this, screen)
        if (flags != null) intent.addFlags(flags)
        if (bundle != null) intent.putExtras(bundle)
        startActivity(intent)
        if (isFinish) finish()
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    open fun endThisActivity() {
        finish()
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    // ============================== LIFECYCLE ===================================
    override fun attachBaseContext(newBase: Context?) {
        val languageCode = SharedPrefs.languageCode
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        val mContext = newBase?.createConfigurationContext(config)
        super.attachBaseContext(mContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView(savedInstanceState)
        observeUI()

        //
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideNavigationBar()
        } else {
            hideNavigationBarLegacy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancelCoroutines()
    }


    // ============================== HIDE UI ===================================
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideNavigationBar() {
        window.insetsController?.apply {
            hide(WindowInsets.Type.navigationBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun hideNavigationBarLegacy() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    // ============================== DIALOG ====================================
    private var loadingDialog: LoadingDialog? = null
    open fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
        }
        loadingDialog?.show(supportFragmentManager, loadingDialog?.tag)
    }

    open fun hideLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }


    open fun showNotifyDialog() {}


    open fun showErrorDialog() {}

}