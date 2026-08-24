package com.creator.workmanagerdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.creator.workmanagerdemo.ui.theme.WorkManagerDemoTheme
import com.creator.workmanagerdemo.worker.MyDemoWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val workManager = WorkManager.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkManagerDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }


//        For one time work request
//        doWork()

//        For periodic work request
        doPeriodicWork()
    }

    private fun doPeriodicWork() {
        val request = PeriodicWorkRequest.Builder(MyDemoWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        workManager.enqueue(request)

        workManager.getWorkInfoByIdLiveData(request.id).observe(this){
            if(it != null){
                printStatus(it.state.name)
            }
        }
    }

    private fun doWork() {
        val request = OneTimeWorkRequest.Builder(MyDemoWorker::class.java)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .setBackoffCriteria(
                BackoffPolicy.LINEAR
                , 15
                , TimeUnit.SECONDS
            )
            .build()
        workManager.enqueue(request)

//        To run multiple requests
     /*   workManager.beginWith(request)
            .then(request)
            .then(request)
            .enqueue()*/

        workManager.getWorkInfoByIdLiveData(request.id).observe(this){
            if(it != null){
                printStatus(it.state.name)
            }
        }

    }

    fun printStatus(name: String) {
        Log.d("PrashantG", name)
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkManagerDemoTheme {
        Greeting("Android")
    }
}