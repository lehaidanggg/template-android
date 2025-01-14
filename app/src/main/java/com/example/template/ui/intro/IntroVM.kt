package com.example.template.ui.intro

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.template.base.viewmodel.BaseViewModel
import com.example.template.common.ResourcesProvider
import com.example.template.data.models.AdKey
import com.example.template.data.models.IntroAD
import com.example.template.data.models.IntroModel
import com.google.android.gms.ads.nativead.NativeAd
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.util.Collections
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class IntroVM @Inject constructor(
    private val resProvider: ResourcesProvider
) : BaseViewModel() {
    val intros = MutableLiveData<MutableList<IntroModel>>()

    private var _intros = Collections.synchronizedList<IntroModel>(mutableListOf())
    private var introModels = Collections.synchronizedList(IntroModel.defaultIntro())


    fun loadNativeAD(context: Context) {
        val handler = CoroutineExceptionHandler { _, throwable ->
            Log.e(tag,"ERR: $throwable")
        }
        parentJob = viewModelScope.launch(handler) {
            supervisorScope {
                getAdUnitIDs().forEachIndexed { _, adIntro ->
                    launch {
                        val adUnitID = if(adIntro.value == -1) "" else resProvider.getString(adIntro.value)
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
                val filteredIntros = filterIntroModels(_intros)
                val sortedIntros = sortIntros(filteredIntros)
                intros.postValue(sortedIntros)
                return@invokeOnCompletion
            }

            // error != null
            val filteredIntros = filterIntroModels(introModels).toMutableList()
            intros.postValue(filteredIntros)
        }
    }

    private fun filterIntroModels(rawList: List<IntroModel>): List<IntroModel> {
        return rawList.filterNot { it.keyAD == AdKey.INTRO_FULL_SCREEN_2 && it.nativeAD == null }
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

    // --------------------------------------------------------------------------------------------
    companion object {
        const val TAG = "IntroVM"

        //
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                val resProvider = ResourcesProvider(application)

                return IntroVM(resProvider) as T
            }
        }
    }
}