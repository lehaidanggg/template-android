package com.example.template.ui.dialogs

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.template.R
import com.example.template.databinding.DialogForceUpdateBinding
interface IClickForeUpdate {
    fun onClickUpdate()
    fun onClickNoThanks()
}

class DialogForceUpdate(
    private val onlyUpdate: Boolean,
    private val listener: IClickForeUpdate
) : DialogFragment() {

    private var _binding: DialogForceUpdateBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.DialogTheme_transparent
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogForceUpdateBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWidthPercent(85)
        isCancelable = false
    }

    private fun setupView() {
        binding.tvNoThanks.visibility = if (onlyUpdate) View.GONE else View.VISIBLE
        binding.tvNoThanks.setOnClickListener {
            listener.onClickNoThanks()
        }
        //
        binding.tvUpdate.setOnClickListener {
            listener.onClickUpdate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}