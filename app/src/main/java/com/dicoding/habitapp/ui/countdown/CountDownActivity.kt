package com.dicoding.habitapp.ui.countdown

import android.os.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.lifecycle.*
import androidx.work.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.*
import com.dicoding.habitapp.notification.*
import com.dicoding.habitapp.utils.*
import java.util.concurrent.*

class CountDownActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down)
        supportActionBar?.title = "Count Down"

        val habit = getParcelableExtra(intent, HABIT, Habit::class.java)

        if (habit != null) {
            findViewById<TextView>(R.id.tv_count_down_title).text = habit.title

            val viewModel = ViewModelProvider(this)[CountDownViewModel::class.java]

            //TODO 10 : Set initial time and observe current time. Update button state when countdown is finished
            viewModel.setInitialTime(habit.minutesFocus)
            viewModel.currentTimeString.observe(this) {
                findViewById<TextView>(R.id.tv_count_down).text = it
            }

            //TODO 13 : Start and cancel One Time Request WorkManager to notify when time is up.
            viewModel.eventCountDownFinish.observe(this) {
                updateButtonState(it)
            }

            findViewById<Button>(R.id.btn_start).setOnClickListener {
                viewModel.startTimer()
                updateButtonState(true)

                val initTime = viewModel.getInitialTime()
                if (initTime != null) {
                    val data = workDataOf(
                        HABIT_ID to habit.id,
                        HABIT_TITLE to habit.title
                    )
                    val notificationWorkManager: WorkRequest =
                        OneTimeWorkRequestBuilder<NotificationWorker>()
                            .setInputData(data)
                            .setInitialDelay(duration = initTime, timeUnit = TimeUnit.MILLISECONDS)
                            .build()
                    WorkManager.getInstance(this).enqueue(notificationWorkManager)
                }
            }

            findViewById<Button>(R.id.btn_stop).setOnClickListener {
                viewModel.resetTimer()
                updateButtonState(false)
                WorkManager.getInstance(this).cancelAllWork()
            }
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        findViewById<Button>(R.id.btn_start).isEnabled = !isRunning
        findViewById<Button>(R.id.btn_stop).isEnabled = isRunning
    }
}