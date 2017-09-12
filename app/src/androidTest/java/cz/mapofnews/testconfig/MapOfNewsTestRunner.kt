package cz.mapofnews.testconfig

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

/**
 * Replaces [AndroidJUnitRunner] by injecting my own application into tests.
 * Specifically [TestApp] instead of App.
 */
class MapOfNewsTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        // return my own instance of application, ready for tests
        return super.newApplication(cl, TestApp::class.java.name, context)
    }

}