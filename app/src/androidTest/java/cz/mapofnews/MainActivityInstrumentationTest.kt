package cz.mapofnews

import android.support.test.InstrumentationRegistry
import android.support.test.filters.SdkSuppress
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.Until
import com.nhaarman.mockito_kotlin.*
import cz.mapofnews.api.AppCallback
import cz.mapofnews.api.RetrieveApi
import cz.mapofnews.service.Event
import cz.mapofnews.service.News
import cz.mapofnews.testconfig.TestApp
import cz.mapofnews.testconfig.TestingData
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class MainActivityInstrumentationTest {

    // logging tag
    private val TAG = MainActivityInstrumentationTest::class.java.name
    // timeout to wait for each action to take efect
    private val DEFAULT_TIMEOUT: Long = 3000
    // reference to the device
    private val device: UiDevice by lazy { UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()) }

    @Inject lateinit var retrieveApi: RetrieveApi

    @Before
    fun setUp() {
        // inject dagger context
        (InstrumentationRegistry.getTargetContext().applicationContext as TestApp).component.inject(this)
        // MOCKS
        // return sample events as an answer to call retrieveApi.retrieveAllEvents()
        doAnswer {
            (it.arguments[0] as AppCallback<List<Event>>).handleResponse(TestingData.events)
        }.whenever(retrieveApi).retrieveAllEvents(any())
        // return sample news as an answer to call retrieveApi.retrieveNews()
        doAnswer {
            (it.arguments[1] as AppCallback<News?>).handleResponse(TestingData.news[it.arguments[0]])
        }.whenever(retrieveApi).retrieveNews(any(), any())

        // go to home screen
        device.pressHome().and(device.wait(Until.hasObject(By.text("MapOfNews")), DEFAULT_TIMEOUT))
        // run the MapOfNews app
        device.findObject(By.text("MapOfNews")).clickAndWait(Until.newWindow(), DEFAULT_TIMEOUT)
    }

    @After
    fun tearDown() {
        // go to home screen
        device.pressHome()
    }


    /**
     * Clicks on marker with accident and checks if the right panel contains the correct information.
     */
    @Test
    fun testMarkerClick() {
        // click on the car accident near brno
        By.desc("Google Map").also {
            device.wait(Until.hasObject(it), DEFAULT_TIMEOUT)
            device.findObject(it).children[2].click()
        }
        // waint until the right panel is visible
        device.wait(Until.hasObject(By.res("cz.mapofnews:id/rightPanel")), DEFAULT_TIMEOUT)

        // assert the title view contains the correct text
        assertEquals(TestingData.events[0].title, device.findObject(By.res("cz.mapofnews:id/titleView")).text)

        // assert the abstract view contains the correct text
        assertEquals(TestingData.events[0].abstract, device.findObject(By.res("cz.mapofnews:id/abstractView")).text)

        // assert the source view contains the correct text
        assertEquals(TestingData.events[0].source, device.findObject(By.res("cz.mapofnews:id/sourceView")).text)

        // assert the date view contains the correct date
        val dateView = device.findObject(By.res("cz.mapofnews:id/dateView"))
        val expectedDate: Date = TestingData.events[0].eventDate
        val expectedDateString: String = SimpleDateFormat(InstrumentationRegistry.getTargetContext().getString(R.string.date_format)).format(expectedDate)
        assertEquals(expectedDateString, dateView.text)
    }

    /**
     * Clicks on update button and checks if the app asked for the data. Using mock interaction.
     */
    @Test
    fun testUpdateBtn() {
        // reset interaction with mock
        reset(retrieveApi)
        // click on the update
        By.res("cz.mapofnews:id/updateBtn").also {
            device.wait(Until.hasObject(it), DEFAULT_TIMEOUT)
            device.findObject(it).click()
        }
        device.waitForIdle(1000)
        // assert that app asked for events
        verify(retrieveApi).retrieveAllEvents(any())
    }

    /**
     * Just clicks on restore button. No idea how to check if the map was restored.
     */
    @Test
    fun testRestoreBtn() {
        // click retore button
        By.res("cz.mapofnews:id/restoreBtn").also {
            device.wait(Until.hasObject(it), DEFAULT_TIMEOUT)
            device.findObject(it).click()
        }
    }

    /**
     * Opens the right panel by clicking on accident marker. Then close it by swipe and checks if it is really gone.
     */
    @Test
    fun testClosingRightPanel() {
        // click on the car accident near brno
        By.desc("Google Map").also {
            device.wait(Until.hasObject(it), DEFAULT_TIMEOUT)
            device.findObject(it).children[2].click()
        }
        // waint until the right panel is visible
        device.wait(Until.hasObject(By.res("cz.mapofnews:id/rightPanel")), DEFAULT_TIMEOUT)

        // slide to close it
        val swipeXFrom = 2 * device.displayWidth / 3
        val swipeXTo = device.displayWidth
        val swipeY = device.displayHeight / 2
        val result = device.swipe(swipeXFrom, swipeY, swipeXTo, swipeY, 30)
        if (!result) {
            fail("Coordinates of swipe are out of screen.")
        }

        assertNull(device.findObject(By.res("cz.mapofnews:id/rightPanel")))
    }


}