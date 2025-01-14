package com.example.template.common

import android.view.View
import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("isHidden")
fun View.setHidden(size: Int) {
    visibility = if (size == 0) View.VISIBLE else View.GONE
}

@BindingAdapter("changeMode")
fun View.setChangeMode(mode: Boolean) {
    visibility = if (mode) View.VISIBLE else View.GONE
}

@BindingAdapter("alpha")
fun View.setAlpha(enable: Boolean) {
    alpha = if (enable) 1f else 0.5f
}

@BindingAdapter("checkedButtonId")
fun setCheckedButtonId(radioGroup: RadioGroup, id: Int?) {
    if (id != null && id != radioGroup.checkedRadioButtonId) {
        radioGroup.check(id)
    }
}

@InverseBindingAdapter(attribute = "checkedButtonId")
fun getCheckedButtonId(radioGroup: RadioGroup): Int {
    return radioGroup.checkedRadioButtonId
}

@BindingAdapter("checkedButtonIdAttrChanged")
fun setCheckedButtonIdListener(radioGroup: RadioGroup, listener: InverseBindingListener?) {
    radioGroup.setOnCheckedChangeListener { _, _ ->
        listener?.onChange()
    }
}


