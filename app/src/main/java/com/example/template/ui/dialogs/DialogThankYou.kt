package com.example.template.ui.dialogs

import android.os.Bundle
import com.example.template.base.dialog.BaseDialogFragment
import com.example.template.databinding.DialogThanksBinding

class DialogThankYou : BaseDialogFragment<DialogThanksBinding>(DialogThanksBinding::inflate) {

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        binding.btnOke.setOnClickListener {
            dismiss()
        }
    }

}



