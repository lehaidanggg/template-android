package com.example.template.ui.intro

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.template.base.viewmodel.BaseViewModel
import com.example.template.common.resource.ResourcesProvider
import com.example.template.data.models.IntroAD
import com.example.template.data.models.IntroModel
import com.google.android.gms.ads.nativead.NativeAd
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.util.Collections
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IntroVM(
    private val resProvider: ResourcesProvider
) : BaseViewModel() {
    val intros = MutableLiveData<MutableList<IntroModel>>()

    private var _intros = Collections.synchronizedList<IntroModel>(mutableListOf())
    private var introModels = Collections.synchronizedList(IntroModel.defaultIntro())


    fun loadNativeAD(context: Context) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            Log.e(tag, "ERR: $throwable")
        }
        parentJob = viewModelScope.launch(handler) {
            supervisorScope {
                getAdUnitIDs().forEachIndexed { _, adIntro ->
                    launch {
                        val adUnitID =
                            if (adIntro.value == -1) "" else resProvider.getString(adIntro.value)
                        val nativeAD = fetchNativeAD(context, adUnitID, true)

                        val indexIntroModel = introModels.indexOfFirst { it.keyAD == adIntro.key }
                        if (indexIntroModel != -1) {
                            val introModel = introModels[indexIntroModel]

                            val lsIntros = _intros
                            _intros.add(
                                introModel.copy(
                                    nativeAD = nativeAD
                                )
                            )

                            _intros = lsIntros
                        }
                    }
                }
            }
        }

        // handle last intros before show ui
        parentJob?.invokeOnCompletion { throwable ->
            // error = null
            if (throwable == null) {
                val sortedIntros = sortIntros(_intros)
                intros.postValue(sortedIntros)
                return@invokeOnCompletion
            }

            // error != null
            intros.postValue(introModels)
        }
    }


    private fun sortIntros(rawIntro: List<IntroModel>): MutableList<IntroModel> {
        return rawIntro.sortedBy { it.keyAD.value }.toMutableList()
    }

    private fun getAdUnitIDs(): List<IntroAD> {
        return IntroAD.getIntroAds()
    }

    private suspend fun fetchNativeAD(
        context: Context,
        adUnitID: String,
        enabled: Boolean,
    ): NativeAd? = withContext(Dispatchers.IO) {
        suspendCoroutine { resume ->
            if (!enabled) {
                resume.resume(null)
                return@suspendCoroutine
            }

            val callback = object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    resume.resume(nativeAd)
                }

                override fun onAdFailedToLoad() {
                    resume.resume(null)
                }
            }

            Admob.getInstance().loadNativeAd(context, adUnitID, callback)
        }
    }
}