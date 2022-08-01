package com.example.workmanagersample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class BroadCastReceiverClass : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val track_market = OneTimeWorkRequest.Builder(RunTask::class.java)
            .setInitialDelay(1, TimeUnit.MINUTES).addTag("Stock_Market").build()
        if (p0 != null) {
            WorkManager.getInstance(p0).enqueue(track_market)
//            val intent = Intent(p0, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            p0.startActivity(intent)
        }
    }
}