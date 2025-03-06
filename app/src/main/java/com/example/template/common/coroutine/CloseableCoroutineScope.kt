package com.example.template.common.coroutine

import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

class CloseableCoroutineScope(
    context: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate
) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context
    override fun close() {
        coroutineContext.cancel()
    }
}