package com.example.workmanagersample

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.workmanagersample.models.ResponseDatum
import com.fitness.modval.network.ApiInterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


class RunTask(context: Context, parameters: WorkerParameters) : Worker(context,parameters) {
    var id = 1;

    override fun doWork(): Result {
        try {
            val url = inputData.getString("url")
            val interval = inputData.getInt("interval",1)
            val preferences : SharedPreferences = applicationContext.getSharedPreferences("runnerTask", Context.MODE_PRIVATE)
            Log.i("Run WorkManager : ", "TaskRun")
            if (Utils.checkForInternet(applicationContext) && url != null) {
                ApiInterface().sendRequest(url).enqueue(object : Callback<ResponseBody>{
                    @SuppressLint("CommitPrefEdits")
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
//                        val data = "[{\"notification_id\":2492,\"event_time\":1645655359721,\"event_type\":\"ALERT_NURSE_CALL\",\"count\":0,\"resolved_at\":null,\"resolved_by\":null,\"dismissed\":null,\"patient\":{\"last_name\":\"TEST\",\"first_name\":\"PATIENT\",\"patient_id\":\"ef60650b-b5c3-42e2-a1ff-5b89e1bdbe2e\",\"escape_monitor\":1,\"weight\":50,\"age\":999,\"height\":2,\"sex\":1,\"assigned_room\":\"Alert Test AP Location \"},\"watch\":{\"watch_mac\":\"c2:bb:8f:4e:ed:68\",\"patient_id\":\"ef60650b-b5c3-42e2-a1ff-5b89e1bdbe2e\",\"watch_model\":\"Bangle2\",\"last_log_collection\":null},\"access_point\":{\"access_point_mac\":\"b8:27:eb:50:9d:d7\",\"description\":\"b8:27:eb:50:9d:d7\",\"near_exit\":0,\"access_point_model\":\"undefined\",\"access_point_ip\":\"192.168.0.224\"}}]"
                        if(response.code() == 200 && !response.body().toString().isNullOrEmpty()) {
                            val type = object : TypeToken<List<ResponseDatum>>() {}.type
                            val dataStr = response.body()!!.string()
                            val modellist = Gson().fromJson<List<ResponseDatum>>(dataStr, type).toList()

                            for(NotifcationData in modellist){
                                var title = ""
                                val body = NotifcationData.eventType
                                if (NotifcationData.accessPoint != null) title = "ALERT @ ${NotifcationData.accessPoint.description}"
                                else title = "ALERT"
                                ShowNotification(title,body,"Information",NotifcationData.notificationId)
                            }
                            StartNewRequest(url,interval)
                            preferences.edit().clear().apply()
                        }else{
                            var count = preferences.getInt("count",0)
                            count++
                            if(count>5){
                                WorkManager.getInstance(applicationContext).cancelAllWork()
                            }
                            else
                                preferences.edit().putInt("count",count)
                        }
                    }

                    @SuppressLint("CommitPrefEdits")
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        var count = preferences.getInt("count",0)
                        count++
                        if(count>5){
                            WorkManager.getInstance(applicationContext).cancelAllWork()
                        }
                        else
                            preferences.edit().putInt("count",count)
                    }
                })
            }
//            ShowNotification("Title","Body","Information",id=+1)
//            StartNewRequest()
            return Result.success();
        }catch (e:Exception){
            Log.i("Run WorkManager : ", "Fail");
            return Result.failure();
        }
    }
    private fun StartNewRequest(url: String, interval: Int) {
        val myData: Data = workDataOf("url" to url,
            "interval" to interval)
        val track_market = OneTimeWorkRequest.Builder(RunTask::class.java)
            .setInputData(myData)
            .setInitialDelay(interval.toLong(), TimeUnit.SECONDS)
            .addTag("Stock_Market")
            .build()
        WorkManager.getInstance(applicationContext).enqueue(track_market)
    }

    private fun ShowNotification(Title: String, Message: String, Information: String, id: Int) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "my_channel_id_01"
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Stock Market",
            NotificationManager.IMPORTANCE_HIGH
        )
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        // Configure the notification channel.
        notificationChannel.description = "Channel description"
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.GREEN
        notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
        notificationChannel.enableVibration(true)
        notificationChannel.setSound(null, null)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
        val notificationBuilder = NotificationCompat.Builder(
            applicationContext, NOTIFICATION_CHANNEL_ID
        )
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(android.R.drawable.alert_dark_frame)
            .setSound(uri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(Title + id)
            .setContentText(Message)
            .setContentInfo(Information)
            .setContentIntent(pendingIntent)
        notificationManager.notify( /*notification id*/id, notificationBuilder.build())
    }
}