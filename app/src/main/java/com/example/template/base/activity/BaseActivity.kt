package com.example.template.base.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.template.base.CoroutineLauncher

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {

    private val activityScope: CoroutineLauncher by lazy {
        return@lazy CoroutineLauncher()
    }

    open val binding: VB by lazy { makeBinding(layoutInflater) }

    abstract fun makeBinding(layoutInflater: LayoutInflater): VB

    open fun observeUI() {}
    // ============================== LIFE CYCLE ===================================

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancelCoroutines()
    }
}