package com.example.template.common.ext

import android.view.View

// ============================== VIEW ===================================
fun View.gone() {
    visibility = View.GONE
    isEnabled = false
}

fun View.visible() {
    visibility = View.VISIBLE
    isEnabled = true
}

fun View.hidden() {
    visibility = View.INVISIBLE
    isEnabled = false
}