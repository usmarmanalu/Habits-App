package com.dicoding.habitapp.notification

import android.app.*
import android.app.TaskStackBuilder
import android.content.*
import android.os.*
import androidx.core.app.*
import androidx.work.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.ui.detail.*
import com.dicoding.habitapp.utils.*

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val habitId = inputData.getInt(HABIT_ID, 0)
    private val habitTitle = inputData.getString(HABIT_TITLE)

    override fun doWork(): Result {
        val prefManager =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify =
            prefManager.getBoolean(applicationContext.getString(R.string.pref_key_notify), false)

        //TODO 12 : If notification preference on, show notification with pending intent
        if (shouldNotify) {
            if (habitTitle != null) {
                val intent = Intent(applicationContext, DetailHabitActivity::class.java)
                intent.putExtra(HABIT_ID, habitId)
                val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                    addNextIntentWithParentStack(intent)
                    getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
                }

                val notificationManagerCompat =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val builder =
                    NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_notifications)
                        .setContentTitle(habitTitle)
                        .setContentText(applicationContext.getString(R.string.notify_content))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "habitsChannel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    builder.setChannelId(NOTIFICATION_CHANNEL_ID)
                    notificationManagerCompat.createNotificationChannel(channel)
                }

                val notification = builder.build()
                notificationManagerCompat.notify(100, notification)
            }
        }

        return Result.success()
    }
}
