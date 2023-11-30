package com.dicoding.habitapp.data

import android.content.*
import androidx.room.*
import com.dicoding.habitapp.R
import kotlinx.coroutines.*
import org.json.*
import java.io.*

//TODO 3 : Define room database class and prepopulate database using JSON
@Database(entities = [Habit::class], version = 1, exportSchema = false)
abstract class HabitDatabase : RoomDatabase() {

    abstract fun habitDao(): HabitDao

    companion object {

        @Volatile
        private var INSTANCE: HabitDatabase? = null

        fun getInstance(context: Context): HabitDatabase {
            return synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habit.db"
                ).build()
                INSTANCE = instance

                val sharedPreferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
                val isLoader = sharedPreferences.getBoolean("isLoaded", false)
                if (isLoader) {
                    sharedPreferences.edit().putBoolean("isLoaded", true).apply()
                    runBlocking {
                        withContext(Dispatchers.IO) {
                            fillWithStartingData(context, instance.habitDao())
                        }
                    }
                }
                instance
            }
        }

        private fun fillWithStartingData(context: Context, dao: HabitDao) {
            val jsonArray = loadJsonArray(context)
            try {
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        dao.insertAll(
                            Habit(
                                item.getInt("id"),
                                item.getString("title"),
                                item.getLong("minutesFocus"),
                                item.getString("startTime"),
                                item.getString("priorityLevel")
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonArray(context: Context): JSONArray? {
            val builder = StringBuilder()
            val `in` = context.resources.openRawResource(R.raw.habit)
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("habits")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }
    }
}
