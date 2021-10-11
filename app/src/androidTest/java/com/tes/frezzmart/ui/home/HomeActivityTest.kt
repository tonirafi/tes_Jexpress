package com.tes.frezzmart.ui.home


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tes.frezzmart.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun homeActivityTest() {
        val actionMenuItemView = onView(
            allOf(
                withId(R.id.search),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val k = onView(
            allOf(
                withId(R.id.edSearch),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withId(R.id.rootLayout),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        k.perform(replaceText("bitc"), closeSoftKeyboard())

        pressBack()

        val f = onView(
            allOf(
                withId(R.id.btReload), withText("Reload"),
                childAtPosition(
                    allOf(
                        withId(R.id.empty_error),
                        childAtPosition(
                            withId(R.id.swipe_target),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        f.perform(click())

        val f2 = onView(
            allOf(
                withId(R.id.btReload), withText("Reload"),
                childAtPosition(
                    allOf(
                        withId(R.id.empty_error),
                        childAtPosition(
                            withId(R.id.swipe_target),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        f2.perform(click())

        val k2 = onView(
            allOf(
                withId(R.id.edSearch), withText("bitc"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withId(R.id.rootLayout),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        k2.perform(longClick())

        val k3 = onView(
            allOf(
                withId(R.id.edSearch), withText("bitc"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withId(R.id.rootLayout),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        k3.perform(replaceText(""))

        val k4 = onView(
            allOf(
                withId(R.id.edSearch),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withId(R.id.rootLayout),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        k4.perform(closeSoftKeyboard())

        val constraintLayout = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.swipe_target),
                        childAtPosition(
                            withId(R.id.swipeToLoadLayout),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        constraintLayout.perform(click())

        val f3 = onView(
            allOf(
                withId(R.id.btReload), withText("Reload"),
                childAtPosition(
                    allOf(
                        withId(R.id.empty_error),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        f3.perform(click())

        val f4 = onView(
            allOf(
                withId(R.id.btReload), withText("Reload"),
                childAtPosition(
                    allOf(
                        withId(R.id.empty_error),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        f4.perform(click())

        val m = onView(
            allOf(
                withContentDescription("Kembali ke atas"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withId(R.id.rootLayout),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        m.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
