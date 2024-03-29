package com.dicoding.habitapp.ui.random

import android.annotation.*
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.data.*

class RandomHabitAdapter(
    private val onClick: (Habit) -> Unit
) : RecyclerView.Adapter<RandomHabitAdapter.PagerViewHolder>() {

    private val habitMap = LinkedHashMap<PageType, Habit>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(key: PageType, habit: Habit) {
        habitMap[key] = habit
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PagerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pager_item, parent, false)
        )

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val key = getIndexKey(position) ?: return
        val pageData = habitMap[key] ?: return
        holder.bind(key, pageData)
    }

    override fun getItemCount() = habitMap.size

    private fun getIndexKey(position: Int) = habitMap.keys.toTypedArray().getOrNull(position)

    enum class PageType {
        HIGH, MEDIUM, LOW
    }

    inner class PagerViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        // Create view and bind data to item view
        fun bind(pageType: PageType, pageData: Habit) {
            val tvTitle = itemView.findViewById<TextView>(R.id.pager_tv_title)
            val tvStartTime = itemView.findViewById<TextView>(R.id.pager_tv_start_time)
            val ivPriorityLevel = itemView.findViewById<ImageView>(R.id.pager_priority_level)
            val tvCountTime = itemView.findViewById<TextView>(R.id.pager_tv_minutes)
            val btnCountDown = itemView.findViewById<Button>(R.id.btn_open_count_down)

            tvTitle.text = pageData.title
            tvStartTime.text = pageData.startTime
            tvCountTime.text = pageData.minutesFocus.toString()

            btnCountDown.setOnClickListener {
                onClick(pageData)
            }

            when (pageType) {
                PageType.LOW -> ivPriorityLevel.setImageResource(R.drawable.ic_priority_low)
                PageType.MEDIUM -> ivPriorityLevel.setImageResource(R.drawable.ic_priority_medium)
                PageType.HIGH -> ivPriorityLevel.setImageResource(R.drawable.ic_priority_high)
            }

        }
    }
}
