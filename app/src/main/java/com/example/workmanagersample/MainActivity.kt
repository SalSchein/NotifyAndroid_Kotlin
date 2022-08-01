package com.example.workmanagersample

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.workmanagersample.databinding.ActivityMainBinding
import com.example.workmanagersample.models.ResponseDatum
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import io.socket.client.Socket
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Socket instance
        val app: WorkManagerApplication = application as WorkManagerApplication
        mSocket = app.getMSocket()
        //connecting socket
        mSocket.let {
            it!!.connect().on(Socket.EVENT_CONNECT) {

            }
        }

        mSocket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
            if (args[0] != null) {

            }
        }

        mSocket?.on("unresolved-notifications") { args ->
            if (args[0] != null) {
                val responseData = args[0] as JSONArray
                Log.i("Response Data",responseData.toString())
                runOnUiThread {
                    // The is where you execute the actions after you receive the data
                    if (responseData != null && responseData.length() > 0) {
                        val type = object : TypeToken<List<ResponseDatum>>() {}.type
                        val dataStr = responseData.toString()
                        val modellist = Gson().fromJson<List<ResponseDatum>>(dataStr, type).toList()

                        for(NotifcationData in modellist){
                            var title = ""
                            val body = NotifcationData.eventType
                            if (NotifcationData.accessPoint != null) title = "ALERT @ ${NotifcationData.accessPoint.description}"
                            else title = "ALERT"
                            ShowNotification(title,body,"Information",NotifcationData.notificationId)
                        }
                    }
                }
            }
        }

        binding.btStart.setOnClickListener {
            mSocket?.connect()
//            setOnTimeWorkRequest("http://192.168.0.153:3000/unresolved-notifications", 15)
            //setOnTimeWorkRequest("http://192.168.0.122/unresolved-notifications", 15)
            // Start a timer service to send a request into the server
//            val intent = Intent(this, NotificationRequestService::class.java)
//            this.startService(intent)
        }
        binding.btStop.setOnClickListener {
            mSocket?.disconnect()
//            cancelWorkRequest()
            // End a timer service
//            val intent = Intent(this, NotificationRequestService::class.java)
//            this.stopService(intent)
        }

//        if(!AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(this)){
//            AutoStartPermissionHelper.getInstance().getAutoStartPermission(this)
//        }

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

    private fun setOnTimeWorkRequest(url:String, interval: Int) {
        try {
            if(URLUtil.isValidUrl(url) && interval > 0) {
                Log.i("MyWork", "Start")
                val myData: Data = workDataOf(
                    "url" to url,
                    "interval" to interval
                )
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val track_market = OneTimeWorkRequest.Builder(RunTask::class.java)
                    .setInputData(myData)
                    .addTag("Stock_Market")
                    .build()
                WorkManager.getInstance(applicationContext).enqueue(track_market)
            }

        }catch (e:Exception){
            Log.i("MyWork", "Fail ${e.toString()}")
        }
    }
    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    private fun setPeriodicWorkRequest(url:String){
        try {
            Log.i("MyWork", "Start")
            if(URLUtil.isValidUrl(url)) {
                Log.i("MyWork", "Start")
                val myData: Data = workDataOf(
                    "url" to url,
                    "interval" to 15
                )

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
                val workRequest = PeriodicWorkRequest.Builder(RunTask::class.java,15,TimeUnit.SECONDS)
                    .setConstraints(constraints)
                    .setInputData(myData)
                    .build()
                //WorkManager.getInstance(applicationContext).enqueue(workRequest)
                WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("NotificationRequestWork", ExistingPeriodicWorkPolicy.KEEP, workRequest)
            }

        }catch (e:Exception){
            Log.i("MyWork", "Fail ${e.toString()}")
        }
    }
    private fun cancelWorkRequest(){
        WorkManager.getInstance(applicationContext).cancelAllWork()
    }
}