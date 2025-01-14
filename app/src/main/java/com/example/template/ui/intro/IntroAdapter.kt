package com.example.template.ui.intro

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.template.R
import com.example.template.common.gone
import com.example.template.common.visible
import com.example.template.data.models.AdKey
import com.example.template.data.models.IntroModel
import com.example.template.databinding.ItemIntroBinding
import com.example.template.databinding.ItemIntroType2Binding
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.util.Admob

interface IIntroAdapterListener {
    fun onClickNext(intro: IntroModel)
}

class IntroAdapter(
    private val items: MutableList<IntroModel>,
    private val listener: IIntroAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_NORMAL = 0
        private const val TYPE_FULL_SCREEN = 1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChanged(items: List<IntroModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): IntroModel {
        return items[position]
    }

    override fun getItemViewType(position: Int): Int {
        val adKey = items[position].keyAD
        return if (adKey == AdKey.INTRO_FULL_SCREEN_2) {
            TYPE_FULL_SCREEN
        } else {
            TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_NORMAL -> {
                val binding = ItemIntroBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return IntroHolderNormal(binding)
            }

            else -> {
                val binding = ItemIntroType2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return IntroHolderFullScreen(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IntroHolderNormal -> holder.bindData(items[position])
            is IntroHolderFullScreen -> holder.bindData(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    inner class IntroHolderNormal(
        private val binding: ItemIntroBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(intro: IntroModel) {
            val vContext = binding.root.context
            with(binding) {
                if (intro.imageRes == 0 || intro.titleRes == 0) {
                    return
                }
                // VIEW
                imgIntro.setImageResource(intro.imageRes)
                tvTitle.text = vContext.getString(intro.titleRes)
                //
                tvNext.setOnClickListener { listener.onClickNext(intro) }

                val keyAd = intro.keyAD
                when (keyAd) {
                    AdKey.INTRO_1 -> {
                        icIndex.setImageResource(R.drawable.ic_index_1)
                        tvNext.text = vContext.getString(R.string.next)
                    }

                    AdKey.INTRO_2 -> {
                        icIndex.setImageResource(R.drawable.ic_index_2)
                        tvNext.text = vContext.getString(R.string.next)
                    }

                    AdKey.INTRO_3 -> {
                        icIndex.setImageResource(R.drawable.ic_index_3)
                        tvNext.text = vContext.getString(R.string.next)
                    }

                    AdKey.INTRO_4 -> {
                        icIndex.setImageResource(R.drawable.ic_index_4)
                        tvNext.text = vContext.getString(R.string.start)
                    }

                    AdKey.INTRO_FULL_SCREEN_2 -> {
                        icIndex.gone()
                        tvNext.gone()
                    }

                }

                // AD
                val nativeAd = intro.nativeAD ?: run {
                    binding.frAd.gone()
                    return
                }
                binding.frAd.visible()
                val resourceNative = if (Admob.getInstance().isLoadFullAds)
                    R.layout.native_ads_media_fullad
                else R.layout.custom_native_ad

                val adView = LayoutInflater.from(vContext).inflate(
                    resourceNative,
                    null
                )

                frAd.removeAllViews()
                frAd.addView(adView)
                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView as NativeAdView)
            }
        }
    }

    inner class IntroHolderFullScreen(
        private val binding: ItemIntroType2Binding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("InflateParams")
        fun bindData(intro: IntroModel) {
            val vContext = binding.root.context
            with(binding) {
                val nativeAd = intro.nativeAD
                nativeAd?.let {
                    val adView = LayoutInflater.from(vContext).inflate(
                        R.layout.native_ad_full_screen,
                        null
                    )

                    frAds.removeAllViews()
                    frAds.addView(adView)
                    Admob.getInstance().pushAdsToViewCustom(it, adView as NativeAdView)
                }
            }
        }
    }

}
