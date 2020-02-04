package com.example.nivapptirgul.ui.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nivapptirgul.R

class AlertReceiver : BroadcastReceiver(){


    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")

        Log.i("TESTING", "the id is , $id")
        createNotification(context, title,body,id )

    }

    private fun createNotification(context: Context, titleStr:String, bodyStr:String, id:Int){
        val builder = NotificationCompat.Builder(context,"Channel-id").apply {
            setContentTitle(titleStr)
            setContentText(bodyStr)
            setSmallIcon(R.drawable.ic_watch_black_24dp)
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(id, builder.build())
    }
}