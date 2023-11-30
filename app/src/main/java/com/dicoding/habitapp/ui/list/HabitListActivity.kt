package com.dicoding.habitapp.ui.list

import android.content.*
import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.appcompat.widget.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.data.*
import com.dicoding.habitapp.setting.*
import com.dicoding.habitapp.ui.*
import com.dicoding.habitapp.ui.add.*
import com.dicoding.habitapp.ui.detail.*
import com.dicoding.habitapp.ui.random.*
import com.dicoding.habitapp.utils.*
import com.google.android.material.floatingactionbutton.*
import com.google.android.material.snackbar.*

class HabitListActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var viewModel: HabitListViewModel
    private lateinit var habitAdapter: HabitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val addIntent = Intent(this, AddHabitActivity::class.java)
            startActivity(addIntent)
        }

        //TODO 6 : Initiate RecyclerView with LayoutManager
        recycler = findViewById(R.id.rv_habit)
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        initAction()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[HabitListViewModel::class.java]

        //TODO 7 : Submit pagedList to adapter and add intent to detail
        habitAdapter = HabitAdapter { habit ->
            val intent = Intent(this, DetailHabitActivity::class.java)
            intent.putExtra(HABIT_ID, habit.id)
            startActivity(intent)
        }

        viewModel.snackbarText.observe(this) {
            showSnackBar(it)
        }

        viewModel.habits.observe(this) {
            habitAdapter.submitList(it)
        }

        recycler.adapter = habitAdapter
    }

    //TODO 15 : Fixing bug : Menu not show and SnackBar not show when list is deleted using swipe
    private fun showSnackBar(eventMessage: Event<Int>) {
        val message = eventMessage.getContentIfNotHandled() ?: return
        Snackbar.make(
            findViewById(R.id.coordinator_layout),
            getString(message),
            Snackbar.LENGTH_SHORT
        ).setAction("Undo") {
            viewModel.insert(viewModel.undo.value?.getContentIfNotHandled() as Habit)
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_random -> {
                val intent = Intent(this, RandomHabitActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_sort -> {
                showSortingPopUpMenu()
                true
            }

            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSortingPopUpMenu() {
        val view = findViewById<View>(R.id.action_sort) ?: return
        PopupMenu(this, view).run {
            menuInflater.inflate(R.menu.sort_habits, menu)

            setOnMenuItemClickListener {
                viewModel.sort(
                    when (it.itemId) {
                        R.id.minutes_focus -> HabitSortType.MINUTES_FOCUS
                        R.id.title_name -> HabitSortType.TITLE_NAME
                        else -> HabitSortType.START_TIME
                    }
                )
                true
            }
            show()
        }
    }

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val habit = (viewHolder as HabitAdapter.HabitViewHolder).getHabit
                viewModel.deleteHabit(habit)
                viewModel.snackbarText.observe(this@HabitListActivity) {
                    showSnackBar(it)
                }
            }

        })
        itemTouchHelper.attachToRecyclerView(recycler)
    }
}
