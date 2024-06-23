package com.example.roomdemo

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.roomdemo.base.EmployeeApp
import com.example.roomdemo.database.EmployeeDao
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var employeeDao: EmployeeDao

    @Before
    fun setup() {
        val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        employeeDao = (appContext.applicationContext as EmployeeApp).database.employeeDao()
    }

    @Test
    fun addEmployee() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btnAdd)).perform(click())

        onView(withId(R.id.etName)).perform(typeText("Bedirhan Saglam"))
        onView(withId(R.id.etEmailId)).perform(typeText("bedirhansaglam270@gmail.com"))

        onView(withId(R.id.btnAdd)).perform(click())

        onView(withId(R.id.rvItemsList)).check(matches(isDisplayed()))

        onView(withId(R.id.rvItemsList)).perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                hasDescendant(withText("Bedirhan Saglam"))
            )
        )
    }
}
