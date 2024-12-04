package com.example.template.base.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.template.databinding.DialogLoadingBinding

class LoadingDialog : BaseDialogFragment<DialogLoadingBinding>() {

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogLoadingBinding {
        return DialogLoadingBinding.inflate(inflater, container, false)
    }

}