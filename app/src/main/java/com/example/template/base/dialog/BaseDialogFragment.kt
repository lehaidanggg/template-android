package com.example.template.base.dialog

import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.example.template.R
import com.example.template.base.fragment.Inflate

abstract class BaseDialogFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : DialogFragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    open fun setupView(savedInstanceState: Bundle?) {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.DialogTheme_transparent
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // hide system ui
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hideNavigationBar()
        } else {
            hideNavigationBarLegacy()
        }

        setWidthPercent(85)
        setupView(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ============================== HIDE UI ===================================
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideNavigationBar() {
        activity?.window?.insetsController?.apply {
            hide(WindowInsets.Type.navigationBars())
//            hide(WindowInsets.Type.systemBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
    private fun hideNavigationBarLegacy() {
        activity?.window?.decorView?.systemUiVisibility = (
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

}

fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}