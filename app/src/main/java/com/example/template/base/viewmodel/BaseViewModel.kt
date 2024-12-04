package com.example.template.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

class BaseViewModel: ViewModel() {

    var mLoading = MutableLiveData<Boolean>(false)
    var mError = MutableLiveData<Exception>()

    val handler = CoroutineExceptionHandler { _, exception ->
//        parseErrorCallApi(exception)
    }



}