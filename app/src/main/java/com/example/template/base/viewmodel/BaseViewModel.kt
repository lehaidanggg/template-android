package com.example.template.base.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.template.common.coroutine.CloseableCoroutineScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel(
    private val coroutineScope: CoroutineScope = CloseableCoroutineScope()
) : ViewModel(coroutineScope) {
    val tag: String = this::class.simpleName.toString()

    var parentJob: Job? = null
        protected set

    var mLoading = MutableLiveData(false)
        protected set

    var mError = MutableLiveData("")
        protected set

    var mNotify = MutableLiveData("")
        protected set

    private var handlerException = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(
            "ERROR-VIEWMODEL",
            "- $tag\n" +
                    "- Context: $coroutineContext\n" +
                    "- Message: ${throwable.message}" +
                    ""
        )
        parseError(throwable)
    }

    var getIoDispatcher = (Dispatchers.IO + handlerException)
    var getMainDispatcher = (Dispatchers.Main.immediate + handlerException)
    var getDefaultDispatcher = (Dispatchers.Default + handlerException)


    protected fun parseError(error: Throwable) {
        val messageError = error.message.toString()
        Log.e(
            "ERROR-VIEWMODEL",
            "- $tag\n" +
                    "- Message: $messageError" +
                    ""
        )
        mError.postValue("Something went wrong! Try again later!")
    }

    protected fun log(message: String) {
        Log.e(tag, message)
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}

fun CoroutineScope.safeLaunch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val coroutine = launch(context, start) {
        if (!isActive) return@launch
        block()
    }
    return coroutine
}