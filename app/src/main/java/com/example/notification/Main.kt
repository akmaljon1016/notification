package com.example.notification

import android.support.v4.os.IResultReceiver
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlin.concurrent.thread

fun main() {
    println("Main program start:${Thread.currentThread().name}")

    val parentJob = CoroutineScope(Default).launch {

        val job1: Deferred<String> = async {
            getData1(Thread.currentThread().name)

        }
        job1.join()
        val job2: Deferred<String> = async {
            getData2(job1.await())
        }
        println(job2.await())
    }
    runBlocking {
        parentJob.join()
    }
    parentJob.invokeOnCompletion {
        it?.let {
            println("parent job failed:${it.message}")
        }?: println("Parent job SUCCESS")
    }
    println("Main program end:${Thread.currentThread().name}")
}

private suspend fun getData1(threadName: String): String {
    println("Fake work1 starts:${threadName}")
    delay(2000)
    println("Fake work1 finished:${threadName}")
    return "Result 1"
}

private suspend fun getData2(threadName: String): String {
    println("Fake work2 starts:${threadName}")
    delay(2000)
    println("Fake work2 finished:${threadName}")
    return "Result 2"
}


fun getData(result: Result) {
    when (result) {
        is Result.Error -> {
            result.showMessage()
        }
        is Result.Loading -> {
            result.showMessage()
        }
        is Result.Success -> {
            result.showMessage()
        }
    }
}

sealed class Result(val message: String) {

    fun showMessage() {
        println(message)
    }

    class Success(message: String) : Result(message)
    class Error(message: String) : Result(message)
    class Loading(message: String) : Result(message)

}