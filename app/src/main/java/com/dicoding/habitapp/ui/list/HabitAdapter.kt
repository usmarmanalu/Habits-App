package com.dicoding.habitapp.ui.list

import android.view.*
import android.widget.*
import androidx.paging.*
import androidx.recyclerview.widget.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.data.*

class HabitAdapter(
    private val onClick: (Habit) -> Unit
) : PagedListAdapter<Habit, HabitAdapter.HabitViewHolder>(DIFF_CALLBACK) {

    // Create and initialize ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.habit_item, parent, false
        )
        return HabitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        // Get data and bind them to ViewHolder
         getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.item_tv_title)
        private val ivPriority: ImageView = itemView.findViewById(R.id.item_priority_level)
        private val tvStartTime: TextView = itemView.findViewById(R.id.item_tv_start_time)
        private val tvMinutes: TextView = itemView.findViewById(R.id.item_tv_minutes)

        lateinit var getHabit: Habit
        fun bind(habit: Habit) {
            getHabit = habit
            tvTitle.text = habit.title
            tvStartTime.text = habit.startTime
            tvMinutes.text = habit.minutesFocus.toString()
            itemView.setOnClickListener {
                onClick(habit)
            }
            when (getHabit.priorityLevel) {
                "Low" -> ivPriority.setImageResource(R.drawable.ic_priority_low)
                "Medium" -> ivPriority.setImageResource(R.drawable.ic_priority_medium)
                "High" -> ivPriority.setImageResource(R.drawable.ic_priority_high)
            }
        }
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
                return oldItem == newItem
            }
        }

    }
}