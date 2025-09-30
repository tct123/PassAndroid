package org.ligi.passandroid

import android.annotation.TargetApi
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.core.IsNot.not
import org.junit.jupiter.api.BeforeEach
import org.junit.Rule
import org.junit.jupiter.api.Test
import org.ligi.passandroid.model.pass.BarCode
import org.ligi.passandroid.model.pass.PassBarCodeFormat
import org.ligi.passandroid.model.pass.PassImpl
import org.ligi.passandroid.model.pass.PassLocation
import org.ligi.passandroid.ui.PassViewActivity
import org.ligi.trulesk.TruleskActivityRule
import org.threeten.bp.ZonedDateTime
import java.util.*

@TargetApi(14)
class ThePassViewActivity {

    private fun getActPass() = TestApp.passStore.currentPass as PassImpl

    @get:Rule
    var rule = TruleskActivityRule(PassViewActivity::class.java, false)

    @BeforeEach
    fun before() {
        TestApp.populatePassStoreWithSinglePass()
    }

    @org.junit.jupiter.api.Test
    fun testThatDescriptionIsThere() {
        rule.launchActivity(null)

        onView(withText(getActPass().description)).check(matches(isDisplayed()))
    }

    @org.junit.jupiter.api.Test
    fun testDateIsGoneWhenPassbookHasNoDate() {
        getActPass().validTimespans = ArrayList()
        rule.launchActivity(null)

        onView(withId(R.id.date)).check(matches(not(isDisplayed())))
    }

    @org.junit.jupiter.api.Test
    fun testEverythingWorksWhenWeHaveSomeLocation() {
        val timeSpen = ArrayList<PassLocation>()
        timeSpen.add(PassLocation())
        getActPass().locations = timeSpen
        rule.launchActivity(null)

        onView(withId(R.id.date)).check(matches(not(isDisplayed())))
    }


    @org.junit.jupiter.api.Test
    fun testDateIsThereWhenPassbookHasDate() {
        getActPass().calendarTimespan = PassImpl.TimeSpan(ZonedDateTime.now(), null, null)
        rule.launchActivity(null)

        onView(withId(R.id.date)).check(matches(isDisplayed()))
    }

    @org.junit.jupiter.api.Test
    fun testLinkToCalendarIsThereWhenPassbookHasDate() {
        getActPass().calendarTimespan = PassImpl.TimeSpan(ZonedDateTime.now(), null, null)
        rule.launchActivity(null)

        onView(withText(R.string.pass_to_calendar)).check(matches(isDisplayed()))
    }

    @org.junit.jupiter.api.Test
    fun testClickOnCalendarWithExpirationDateGivesWarning() {
        val validTimespans = ArrayList<PassImpl.TimeSpan>()
        validTimespans.add(PassImpl.TimeSpan(null, ZonedDateTime.now().minusHours(12), null))
        getActPass().validTimespans = validTimespans
        getActPass().calendarTimespan = null
        rule.launchActivity(null)

        onView(withText(R.string.pass_to_calendar)).perform(click())

        onView(withText(R.string.expiration_date_to_calendar_warning_message)).check(matches(isDisplayed()))
    }

    @Test
    fun testThatTheDialogCanBeDismissed() {
        testClickOnCalendarWithExpirationDateGivesWarning()

        onView(withText(android.R.string.cancel)).perform(click())

        onView(withText(R.string.expiration_date_to_calendar_warning_message)).check(doesNotExist())
    }

    @org.junit.jupiter.api.Test
    fun testLinkToCalendarIsNotThereWhenPassbookHasNoDate() {
        getActPass().validTimespans = ArrayList()
        rule.launchActivity(null)

        onView(withText(R.string.pass_to_calendar)).check(matches(not(isDisplayed())))
    }

    @org.junit.jupiter.api.Test
    fun testClickOnBarcodeOpensFullscreenImage() {
        getActPass().barCode = BarCode(PassBarCodeFormat.QR_CODE, "foo")
        rule.launchActivity(null)
        onView(withId(R.id.barcode_img)).perform(click())

        onView(withId(R.id.fullscreen_barcode)).check(matches(isDisplayed()))
    }

    @org.junit.jupiter.api.Test
    fun testZoomControlsAreThereWithBarcode() {
        getActPass().barCode = BarCode(PassBarCodeFormat.AZTEC, "foo")
        rule.launchActivity(null)

        onView(withId(R.id.zoomIn)).check(matches(isDisplayed()))
        onView(withId(R.id.zoomIn)).check(matches(isDisplayed()))
    }

    @org.junit.jupiter.api.Test
    fun testZoomControlsAreGoneWithoutBarcode() {
        getActPass().barCode = null
        rule.launchActivity(null)

        onView(withId(R.id.zoomIn)).check(matches(not(isDisplayed())))
        onView(withId(R.id.zoomIn)).check(matches(not(isDisplayed())))
    }

}
