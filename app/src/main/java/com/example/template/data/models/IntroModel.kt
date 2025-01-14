package com.example.template.data.models

import com.example.template.R
import com.example.template.data.SharedPrefs
import com.google.android.gms.ads.nativead.NativeAd
import com.nlbn.ads.util.Admob

enum class AdKey(val value: Int) {
    INTRO_1(1),  INTRO_2(2), INTRO_FULL_SCREEN_2(3), INTRO_3(4), INTRO_4(5);
}

data class IntroAD(
    val key: AdKey,
    val value: Int
) {
    companion object {
        fun getIntroAds(): List<IntroAD> {
            val ads = mutableListOf<IntroAD>()
            val isFirstInstall = SharedPrefs.isFirstInstall
            val isFullAD = Admob.getInstance().isLoadFullAds

            ads.add(IntroAD(AdKey.INTRO_1, getUnitIDIntro1(isFirstInstall)))
//            if (isFullAD && isFirstInstall) {
//                ads.add(IntroAD(AdKey.INTRO_FULL_SCREEN_2, getUnitIDIntro2FS()))
//            }
            ads.add(IntroAD(AdKey.INTRO_2, getUnitIDIntro2()))
            ads.add(IntroAD(AdKey.INTRO_3, getUnitIDIntro3(isFirstInstall)))
            ads.add(IntroAD(AdKey.INTRO_4, getUnitIDIntro4(isFirstInstall)))

            return ads
        }

        private fun getUnitIDIntro1(isLoadFullAD: Boolean) : Int {
            if (!isLoadFullAD) return -1

            return R.string.native_intro_1
        }
        private fun getUnitIDIntro2() : Int {
            return R.string.native_intro_2
        }
        private fun getUnitIDIntro3(isLoadFullAD: Boolean) : Int {
            if (!isLoadFullAD) return -1
            return R.string.native_intro_3
        }
        private fun getUnitIDIntro4(isLoadFullAD: Boolean) : Int {
            if (!isLoadFullAD) return -1
            return R.string.native_intro_4
        }
    }
}

data class IntroModel(
    val titleRes: Int,
    val imageRes: Int,
    val keyAD: AdKey,
    val nativeAD: NativeAd? = null
) {
    companion object {
        fun defaultIntro(): MutableList<IntroModel> {
            val intros = mutableListOf<IntroModel>()
            val isFirstInstall = SharedPrefs.isFirstInstall
            val isFullAD = Admob.getInstance().isLoadFullAds

            intros.add(
                IntroModel(
                    R.string.title_intro1,
                    R.drawable.img_intro1,
                    AdKey.INTRO_1
                )
            )
//            if (isFirstInstall && isFullAD) {
//                intros.add(
//                    IntroModel(
//                        0,
//                        0,
//                        AdKey.INTRO_FULL_SCREEN_2,
//                    )
//                )
//            }
            intros.add(
                IntroModel(
                    R.string.title_intro2,
                    R.drawable.img_intro2,
                    AdKey.INTRO_2
                )
            )

            intros.add(
                IntroModel(
                    R.string.title_intro3,
                    R.drawable.img_intro3,
                    AdKey.INTRO_3
                )
            )

            intros.add(
                IntroModel(
                    R.string.title_intro4,
                    R.drawable.img_intro4,
                    AdKey.INTRO_4
                )
            )

            return intros
        }
    }
}