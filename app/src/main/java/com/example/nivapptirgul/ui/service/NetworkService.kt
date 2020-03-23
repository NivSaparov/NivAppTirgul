package com.example.nivapptirgul.ui.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nivapptirgul.R
import com.example.nivapptirgul.data.Repository.DataRepository
import com.example.nivapptirgul.data.db.entity.Reminder
import com.example.nivapptirgul.di.DaggerAppComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG_RECEIVER = "AlertReceiver"

const val TAG_SERVICE = "Network Service"
const val NOTIFICATION_ID_SERVICE = 543

class NetworkService : Service() {

    @Inject
    lateinit var dataRepository: DataRepository
    lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.factory().create(applicationContext).inject(this)
        dataRepository.userRemindersData.observeForever { reminders ->
            scheduleNotification(reminders)
        }
        appContext = this
        // login our user, because we want to keep everything sync
        GlobalScope.launch {
            dataRepository.loginWithoutNetwork()
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.i("MyService", "NetworkService-onTaskRemoved")
        // When service die, we just start new one.
        val restartServiceTask = Intent(appContext, NetworkService::class.java)
        restartServiceTask.setPackage(packageName)

        val restartPendingIntent =
            PendingIntent.getService(appContext, 1, restartServiceTask, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartPendingIntent
        )
        super.onTaskRemoved(rootIntent)
    }

    private fun scheduleNotification(reminders: List<Reminder>) {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (!reminders.isNullOrEmpty())
            for (item in reminders) {
                val alertIntent = Intent(this, AlertReceiver::class.java)
                alertIntent.apply {
                    putExtra("id", item.id)
                    putExtra("title", item.title)
                    putExtra("body", item.body)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    item.id,
                    alertIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    item.date.time - item.beforeTimeMill,
                    pendingIntent
                )
            }
    }


    class AlertReceiver : BroadcastReceiver() {


        override fun onReceive(context: Context, intent: Intent) {
            Log.i("MyService", "AlertReceiver got new notification")
            val id = intent.getIntExtra("id", -1)
            val title = intent.getStringExtra("title")
            val body = intent.getStringExtra("body")
            createNotification(context, title, body, id)

        }

        private fun createNotification(
            context: Context,
            titleStr: String,
            bodyStr: String,
            id: Int
        ) {
            val builder = NotificationCompat.Builder(context, "Channel-id").apply {
                setContentTitle(titleStr)
                setContentText(bodyStr)
                setSmallIcon(R.drawable.ic_watch_black_24dp)
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(id, builder.build())
        }

    }
}
