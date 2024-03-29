package com.dicoding.habitapp.ui.add

import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.lifecycle.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.data.*
import com.dicoding.habitapp.databinding.*
import com.dicoding.habitapp.ui.*
import com.dicoding.habitapp.utils.*
import java.text.*
import java.util.*

class AddHabitActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddHabitViewModel
    private lateinit var binding: ActivityAddHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.add_habit)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AddHabitViewModel::class.java]

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                finish()
                true
            }

            R.id.action_save -> {
                val title = findViewById<EditText>(R.id.add_ed_title).text.toString()
                val minutesFocus =
                    findViewById<EditText>(R.id.add_ed_minutes_focus).text.toString()
                val startTime = findViewById<TextView>(R.id.add_tv_start_time).text.toString()
                val priorityLevel =
                    findViewById<Spinner>(R.id.sp_priority_level).selectedItem.toString()



                if (title.isNotEmpty() && minutesFocus.isNotEmpty() && startTime.isNotEmpty() && priorityLevel.isNotEmpty()) {

                    val isDuplicate = viewModel.isDuplicateHabit(startTime, minutesFocus.toLong())

                    if (isDuplicate) {
                        binding.messageIsDuplicat.visibility = View.VISIBLE
                        binding.messageIsDuplicat.text = getString(R.string.duplicate_habit_message)
                    } else {
                        binding.messageIsDuplicat.visibility = View.GONE
                        val habit = Habit(
                            title = title,
                            minutesFocus = minutesFocus.toLong(),
                            startTime = startTime,
                            priorityLevel = priorityLevel
                        )
                        viewModel.saveHabit(habit)
                        finish()
                    }
                } else {
                    Toast.makeText(this, getString(R.string.empty_message), Toast.LENGTH_SHORT)
                        .show()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showTimePicker(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "timePicker")
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_start_time).text = dateFormat.format(calendar.time)

        binding.messageIsDuplicat.visibility = View.GONE
    }
}
