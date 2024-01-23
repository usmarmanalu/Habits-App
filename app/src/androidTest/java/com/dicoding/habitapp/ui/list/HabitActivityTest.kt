package com.dicoding.habitapp.ui.list

import androidx.test.core.app.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.*
import com.dicoding.habitapp.*
import com.dicoding.habitapp.R.*
import com.dicoding.habitapp.ui.add.*
import org.junit.*
import org.junit.runner.*

// Write UI test to validate when user tap Add Habit (+), the AddHabitActivity displayed
@RunWith(AndroidJUnit4ClassRunner::class)
class HabitActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(HabitListActivity::class.java)
        Intents.init()
    }

    @After
    fun destroy() {
        Intents.release()
    }

    @Test
    fun validateOnClickAddHabits() {
        onView(withId(id.fab)).perform(click())
        intended(hasComponent(AddHabitActivity::class.java.name))
    }
}