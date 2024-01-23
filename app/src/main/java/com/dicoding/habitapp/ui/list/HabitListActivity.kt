package com.dicoding.habitapp.ui.list

import android.annotation.*
import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import android.widget.PopupMenu
import androidx.appcompat.app.*
import androidx.appcompat.widget.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.*
import androidx.preference.*
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.R
import com.dicoding.habitapp.data.*
import com.dicoding.habitapp.data.repository.*
import com.dicoding.habitapp.databinding.*
import com.dicoding.habitapp.setting.*
import com.dicoding.habitapp.ui.*
import com.dicoding.habitapp.ui.add.*
import com.dicoding.habitapp.ui.detail.*
import com.dicoding.habitapp.ui.login.*
import com.dicoding.habitapp.ui.news.*
import com.dicoding.habitapp.ui.random.*
import com.dicoding.habitapp.utils.*
import com.google.android.material.floatingactionbutton.*
import com.google.android.material.snackbar.*
import org.koin.android.ext.android.*
import java.util.*

class HabitListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitListBinding
    private lateinit var recycler: RecyclerView
    private lateinit var viewModel: HabitListViewModel
    private lateinit var habitAdapter: HabitAdapter
    private lateinit var searchView: SearchView
    private var isFirstLoad = true
    private val userRepository: UserRepository by inject()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        binding.swipeRefreshLayout.setOnRefreshListener {
            dataHabit()
        }

        val tvWelcome = findViewById<TextView>(R.id.tv_welcome)
        tvWelcome.text = "Welcome, ${userRepository.getUser()}"

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val addIntent = Intent(this, AddHabitActivity::class.java)
            startActivity(addIntent)
        }

        val btnNews = findViewById<Button>(R.id.btnNews)
        btnNews.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        // Initiate RecyclerView with LayoutManager
        recycler = findViewById(R.id.rv_habit)
        recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        initAction()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[HabitListViewModel::class.java]

        // Submit pagedList to adapter and add intent to detail
        habitAdapter = HabitAdapter { habit: Habit ->
            val intent = Intent(this, DetailHabitActivity::class.java)
            intent.putExtra(HABIT_ID, habit.id)
            startActivity(intent)
        }

        recycler.adapter = habitAdapter

        viewModel.snackbarText.observe(this) {
            showSnackBar(it)
        }

        val theme = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(getString(R.string.pref_key_dark), null)
        theme?.let {
            val themeSelected = when ((it).uppercase(Locale.ROOT)) {
                DarkMode.ON.name -> DarkMode.ON
                DarkMode.OFF.name -> DarkMode.OFF
                else -> DarkMode.FOLLOW_SYSTEM
            }
            AppCompatDelegate.setDefaultNightMode(themeSelected.value)
        }

        // Inisialisasi SearchView
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // If the search query is empty, hide the TextView and show the RecyclerView
                    hideNoResultsMessage()
                    recycler.visibility = View.VISIBLE
                } else {
                    // If there is a search query, update the search results
                    viewModel.searchHabits(newText)
                        .observe(this@HabitListActivity) { searchResults ->
                            if (searchResults.isNotEmpty()) {
                                // Update your UI with the search results
                                habitAdapter.submitList(searchResults)
                                hideNoResultsMessage()
                            } else {
                                // Display a message indicating no results found
                                showNoResultsMessage()
                            }
                        }
                }
                return true
            }
        })
        dataHabit()
    }

    private fun dataHabit() {
        // Show loading indicator only if it's not the initial load
        if (!isFirstLoad) {
            isLoadingHabitList(true)
        }
        // Refresh habits if it's not the initial load
        if (!isFirstLoad) {
            viewModel.refreshHabits()
        }
        // Observe the habits only once after the refresh
        viewModel.habits.observe(this) { habits ->
            // Check if the list is not empty before submitting to the adapter
            if (habits.isNotEmpty()) {
                hideNoResultsMessage()
                habitAdapter.submitList(habits)
            } else {
                // If the list is empty, display a message
                showNoResultsMessage()
            }
            isLoadingHabitList(false)
            binding.swipeRefreshLayout.isRefreshing = false
            // Set isFirstLoad to false after the initial load
            isFirstLoad = false
        }
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showNoResultsMessage() {
        val tvResultMessage = findViewById<TextView>(R.id.tv_no_results_message)
        tvResultMessage.visibility = View.VISIBLE
        tvResultMessage.text = getString(R.string.no_results_message)
        recycler.visibility = View.GONE
    }

    private fun hideNoResultsMessage() {
        val tvResultMessage = findViewById<TextView>(R.id.tv_no_results_message)
        tvResultMessage.visibility = View.GONE
        recycler.visibility = View.VISIBLE
    }

    // Fixing bug : Menu not show and SnackBar not show when list is deleted using swipe
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

            R.id.action_auth -> {
                userRepository.logoutUser()
                moveToMainActivity()
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

    private fun isLoadingHabitList(isLoading: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progresBarHabit)
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
