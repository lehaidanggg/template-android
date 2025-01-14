package com.example.template.base.dialog

import android.os.Bundle
import com.example.template.databinding.DialogNotifyBinding

class NotifyDialog: BaseDialogFragment<DialogNotifyBinding>(DialogNotifyBinding::inflate) {

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

}