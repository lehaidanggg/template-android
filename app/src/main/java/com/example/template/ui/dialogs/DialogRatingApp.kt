package com.example.template.ui.dialogs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.template.BuildConfig
import com.example.template.R
import com.example.template.base.dialog.BaseDialogFragment
import com.example.template.databinding.DialogRatingBinding

class DialogRatingApp(
    private val onRateClickListener: OnRateClickListener?
) : BaseDialogFragment<DialogRatingBinding>(DialogRatingBinding::inflate) {

    private var indexStar = 4
    private var isSelected = false
    private var isFromMenu: Boolean = false

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        val listStars = arrayOf(
            binding.imgStar1,
            binding.imgStar2,
            binding.imgStar3,
            binding.imgStar4,
            binding.img5Star,
        )

        listStars.forEachIndexed { index, view ->
            view.setOnClickListener {
                if (indexStar == index && isSelected) {
                    return@setOnClickListener
                }
                indexStar = index
                isSelected = true

                binding.tvTitle.text = getString(R.string.thanks_for_rating)
                binding.tvDescription.text = genDescription(index)
                checkStarRate(index)

                val isSelected = BooleanArray(listStars.size)
                if (!isSelected[index]) {
                    if (view.scaleX == 1f && view.scaleY == 1f) {
                        view.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.zoom_out
                            )
                        )
                    } else {
                        view.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.zoom_in
                            )
                        )
                    }
                }
            }
        }

        binding.btnNotNow.setOnClickListener {
            dismiss()
        }
        binding.btnRate.setOnClickListener {
//            SharePrefUtils.forceRated()
            rateUS(indexStar + 1)
            onRateClickListener?.onRateClick(!isFromMenu)
        }
    }

    private fun navigateToStore() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        }
    }

    private fun genDescription(index: Int): String {
        return when (index) {
            0 -> getString(R.string.give_us_a_quick_rating)
            1 -> getString(R.string.we_are_working_hard)
            3 -> getString(R.string.we_are_working_hard)
            4 -> getString(R.string.that_great_to_hear)
            5 -> getString(R.string.that_great_to_hear)
            else -> getString(R.string.we_are_working_hard)

        }
    }


//    private fun initReviewInApp() {
//        reviewManager = ReviewManagerFactory.create(requireContext())
//        val request = reviewManager.requestReviewFlow()
//        request.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                reviewInfo = task.result
//            }
//        }
//    }

    private fun rateUS(i: Int) {
        if (i == 5 || i == 4) {
//            launchReviewFlow()
//            SharedPrefs.setRating(i)
            navigateToStore()
            dismiss()
        } else {
            dismiss()
            onRateClickListener?.onRateLessFourStart()
        }
    }

//    private fun launchReviewFlow() {
//        if (reviewInfo != null) {
//            val flow = reviewManager.launchReviewFlow(requireActivity(), reviewInfo!!)
//            flow.addOnCompleteListener { _ ->
//                dismiss()
//            }
//        } else {
//            // Review info is not yet available
//        }
//    }

    private fun checkStarRate(index: Int) {
        val starImages = listOf(
            R.drawable.star_fill,
            R.drawable.star_none
        )

        val statusIcons = listOf(
            R.drawable.ic_rating_smile_2,
            R.drawable.ic_rating_smile_3,
            R.drawable.ic_rating_smile_4,
            R.drawable.ic_rating_smile_5,
            R.drawable.ic_rating_smile_6
        )

        val starImageViews = listOf(
            binding.imgStar1,
            binding.imgStar2,
            binding.imgStar3,
            binding.imgStar4,
            binding.img5Star
        )

        for (i in 0..4) {
            starImageViews[i].setImageResource(starImages[if (i <= index) 0 else 1])
        }

        binding.imgStatus.setImageResource(statusIcons[index])
//        binding.txtOhno.text = requireActivity().resources.getText(textResources[index])
//        binding.txtDes.text = requireActivity().resources.getText(desResources[index])
    }

    fun setFromYakin(isFrom: Boolean) {
        isFromMenu = isFrom
    }

    companion object {
//        fun show(fragmentManager: FragmentManager) {
//            val dialog = DialogRatingApp()
//            dialog.show(fragmentManager, "Dialog Rating")
//        }
    }
}

interface OnRateClickListener {
    fun onRateLessFourStart()
    fun onRateClick(isNeedFinish: Boolean)
}



