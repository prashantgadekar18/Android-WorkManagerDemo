package com.creator.workmanagerdemo.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyDemoWorker(context : Context, param : WorkerParameters) : Worker(context, param) {
    override fun doWork(): Result {
        performWork()
//        return Result.success()
        return Result.retry()
    }

    fun performWork(){
        Thread.sleep(2000)
        Log.d("PrashantG", "Task is complete.")
    }
}