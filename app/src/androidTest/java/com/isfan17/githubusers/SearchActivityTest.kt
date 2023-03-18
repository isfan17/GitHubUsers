package com.isfan17.githubusers

import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.isfan17.githubusers.ui.search.SearchActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SearchActivityTest {

    private val dummyQuery = "asjdjakjdka"

    @Before
    fun setup(){
        ActivityScenario.launch(SearchActivity::class.java)
    }

    @Test
    fun assertGetNoResultMsg() {
        onView(withId(R.id.search_view)).check(matches(isDisplayed()))
        onView(isAssignableFrom(SearchView::class.java)).perform(click())

        onView(isAssignableFrom(EditText::class.java))
            .perform(typeText(dummyQuery), pressImeActionButton())

        Thread.sleep(1000)

        onView(withId(R.id.iv_no_result)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_no_result)).check(matches(isDisplayed()))
    }
}