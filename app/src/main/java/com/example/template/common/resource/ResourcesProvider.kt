package com.example.template.common.resource

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable

class ResourcesProvider(
    val context: Context
) {
    fun getContentResolver(): ContentResolver = context.contentResolver

    fun getString(id: Int): String = context.getString(id)

    fun getString(id: Int, vararg formatArgs: Any): String = context.getString(id)

    fun getColor(id: Int): Int = context.getColor(id)

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawable(id: Int): Drawable? = context.getDrawable(id)
}