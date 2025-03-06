package com.example.template.ui.intro

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.template.R
import com.example.template.common.ext.gone
import com.example.template.common.ext.visible
import com.example.template.data.models.AdKey
import com.example.template.data.models.IntroModel
import com.example.template.databinding.ItemIntroBinding
import com.google.android.gms.ads.nativead.NativeAdView
import com.nlbn.ads.util.Admob

interface IIntroAdapterListener {
    fun onClickNext(intro: IntroModel)
}

class IntroAdapter(
    private val items: MutableList<IntroModel>,
    private val listener: IIntroAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun dataSetChanged(items: List<IntroModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): IntroModel {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IntroHolderNormal(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IntroHolderNormal -> holder.bindData(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    inner class IntroHolderNormal(
        private val binding: ItemIntroBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(intro: IntroModel) {
            val vContext = binding.root.context
            binding.apply {
                if (intro.imageRes == 0 || intro.titleRes == 0) {
                    return
                }
                // VIEW
                imgIntro.setImageResource(intro.imageRes)
                tvTitle.text = vContext.getString(intro.titleRes)
                //
                tvNext.text = vContext.getString(R.string.next)
                tvNext.setOnClickListener { listener.onClickNext(intro) }

                // index
                val resIndex = getResourceIndex(intro.keyAD)
                icIndex.setImageResource(resIndex)

                // AD
                val nativeAd = intro.nativeAD ?: run {
                    binding.frAd.gone()
                    return
                }
                binding.frAd.visible()
                val resourceNative = if (Admob.getInstance().isLoadFullAds)
                    R.layout.ads_media_fullad
                else R.layout.ads_media_normal

                val adView = LayoutInflater.from(vContext).inflate(
                    resourceNative,
                    null
                )

                frAd.removeAllViews()
                frAd.addView(adView)
                Admob.getInstance().pushAdsToViewCustom(nativeAd, adView as NativeAdView)
            }
        }

        private fun getResourceIndex(keyAd: AdKey) : Int {
            return when (keyAd) {
                AdKey.INTRO_1 -> {
                    R.drawable.ic_index_1
                }

                AdKey.INTRO_2 -> {
                    R.drawable.ic_index_2
                }

                AdKey.INTRO_3 -> {
                    R.drawable.ic_index_3
                }

                AdKey.INTRO_4 -> {
                    R.drawable.ic_index_4
                }
            }
        }
    }
}
