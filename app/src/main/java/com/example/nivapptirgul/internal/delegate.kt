package com.example.nivapptirgul.internal

import kotlinx.coroutines.*

fun <T> lazeDeferred(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>>{
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}