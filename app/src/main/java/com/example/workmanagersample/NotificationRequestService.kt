package com.example.workmanagersample

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.workmanagersample.models.ResponseDatum
import com.fitness.modval.network.ApiInterface
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.timerTask


class NotificationRequestService : Service() {

    private val timer: Timer = Timer()
    private var ctx: Context? = null

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this
        startService()
    }

    private fun startService() {
        // Send the request to local server per 15 seconds.
        timer.schedule(timerTask {
//            val url = "http://192.168.0.243:3000/unresolved-notifications"
            val url = "http://192.168.0.122/unresolved-notifications"
            Log.i("Run WorkManager : ", "TaskRun")
            if (Utils.checkForInternet(applicationContext)) {
                ApiInterface().sendRequest(url).enqueue(object : Callback<ResponseBody> {
                    @SuppressLint("CommitPrefEdits")
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.code() == 200 && !response.body().toString().isNullOrEmpty()) {
                            val type = object : TypeToken<List<ResponseDatum>>() {}.type
                            val dataStr = response.body()!!.string()
                            if (dataStr.isNullOrEmpty()) {
                                return
                            }
                            val modellist = Gson().fromJson<List<ResponseDatum>>(dataStr, type).toList()

                            for(NotifcationData in modellist){
                                var title = ""
                                val body = NotifcationData.eventType
                                if (NotifcationData.accessPoint != null)
                                    //title = "ALERT @ ${NotifcationData.accessPoint.description}"
                                    title = "ALERT"
                                else title = "ALERT"
                                Handler(Looper.getMainLooper()).postDelayed({
                                    // Your Code
                                    showNotification(title,body,"Information",NotifcationData.notificationId)
                                }, 0)

                            }
                        } else {
                            doCountFailure()
                        }
                    }

                    @SuppressLint("CommitPrefEdits")
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        doCountFailure()
                    }
                })
            }
        }, 0, 15000)
    }

    private fun doCountFailure() {
        val preferences : SharedPreferences = applicationContext.getSharedPreferences("runnerTask", Context.MODE_PRIVATE)
        var count = preferences.getInt("count",0)
        count++
        if(count>5) {
            timer.cancel()
            stopSelf()
        }
        else
            preferences.edit().putInt("count",count)
    }

    //Display the notification data from the server
    private fun showNotification(Title: String, Message: String, Information: String, id: Int) {
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

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        notificationChannel.setSound(soundUri, audioAttributes)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
        val notificationBuilder = NotificationCompat.Builder(
            applicationContext, NOTIFICATION_CHANNEL_ID
        )
        val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_LIGHTS)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(android.R.drawable.alert_dark_frame)
            .setSound(uri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(Title)
            .setContentText(Message)
            .setContentInfo(Information)
            .setContentIntent(pendingIntent)
        notificationManager.notify( /*notification id*/id, notificationBuilder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show()
    }
}