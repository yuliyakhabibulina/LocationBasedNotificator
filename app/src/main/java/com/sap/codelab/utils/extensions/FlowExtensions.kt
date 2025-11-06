package com.sap.codelab.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Extension function for collecting a flow in a lifecycle-aware manner.
 */
fun <T> LifecycleOwner.collectFlow(flow: Flow<T>, collector: suspend (T) -> Unit) {
    lifecycleScope.launch {
        flow.collect { value ->
            collector(value)
        }
    }
}