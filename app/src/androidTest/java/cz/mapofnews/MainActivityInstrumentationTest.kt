package cz.mapofnews

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.BySelector
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiObject2
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentationTest {

    private lateinit var device: UiDevice

    @Before
    fun before() {
        // initialize device
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        assertThat(device, notNullValue())

        // start from the home screen
        device.pressHome()
    }

    @Test
    @Throws(InterruptedException::class)
    fun markerClicked() {
        openApp("cz.mapofnews")
    }

    private fun openApp(packageName: String) {
        val context = InstrumentationRegistry.getInstrumentation().context
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    @Throws(InterruptedException::class)
    private fun waitForObject(selector: BySelector): UiObject2 {
        var uiObject: UiObject2? = null
        val timeout = 30000
        val delay = 1000
        val time = System.currentTimeMillis()
        while (uiObject == null) {
            uiObject = device.findObject(selector)
            Thread.sleep(delay.toLong())
            if (System.currentTimeMillis() - timeout > time) {
                fail()
            }
        }
        return uiObject
    }
}
