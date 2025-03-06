package com.example.template.base.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.template.base.dialog.LoadingDialog
import com.example.template.common.coroutine.CoroutineLauncher
import com.example.template.data.SharedPrefs
import java.util.Locale

typealias MActivity = BaseActivity<*>
typealias InflateActivity<T> = (LayoutInflater) -> T

abstract class BaseActivity<VB : ViewBinding>(
    private val inflate: InflateActivity<VB>
) : AppCompatActivity() {

    private val activityScope: CoroutineLauncher by lazy {
        return@lazy CoroutineLauncher()
    }
    private val callbackBackPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onNewBackPress()
        }
    }

    private var _binding: VB? = null
    val binding get() = _binding!!

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
        _binding = inflate.invoke(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(
            binding.root
        ) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        // hide system ui
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideNavigationBar()
        } else {
            hideNavigationBarLegacy()
        }

        // other func
        onBackPressedDispatcher.addCallback(this, callbackBackPress)
        setupView(savedInstanceState)
        observeUI()
        setupListener()

    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancelCoroutines()
        _binding = null
    }


    // ============================= OPEN FUNC ====================================
    open fun setupView(savedInstanceState: Bundle?) {}
    open fun observeUI() {}
    open fun setupListener() {}
    open fun onNewBackPress() {}


    // ============================== PERMISSION ================================
    protected var requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val grantedPermissions = permissions.filterValues { it }
        val deniedPermissions = permissions.filterValues { !it }

        if (grantedPermissions.isNotEmpty()) {
            onPermissionsGranted(grantedPermissions)
        }
        if (deniedPermissions.isNotEmpty()) {
            onPermissionsDenied(deniedPermissions)
        }
    }

    protected var registerForActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::onResultRequestOverlayPermission
    )

    open fun isPermissionOverlayGranted(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    open fun requestOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${packageName}")
            )
            registerForActivityResult.launch(intent)
        }
    }

    open fun onPermissionsGranted(grantedPermissions: Map<String, Boolean>) {}

    open fun onPermissionsDenied(deniedPermissions: Map<String, Boolean>) {}

    open fun onResultRequestOverlayPermission(result: ActivityResult) {}


    // ============================== HIDE UI ===================================
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideNavigationBar() {
        window.insetsController?.apply {
            hide(WindowInsets.Type.navigationBars())
//            hide(WindowInsets.Type.systemBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun hideNavigationBarLegacy() {
        window.decorView.systemUiVisibility = (
//                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                        View.SYSTEM_UI_FLAG_FULLSCREEN or
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
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