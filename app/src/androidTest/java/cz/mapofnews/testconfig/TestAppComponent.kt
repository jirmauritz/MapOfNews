package cz.mapofnews.testconfig

import cz.mapofnews.MainActivityInstrumentationTest
import cz.mapofnews.config.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        TestRetrieveApiModule::class,
        TestAppModule::class))
interface TestAppComponent : AndroidInjector<App> {
    fun inject(mainActivityInstrumentationTest: MainActivityInstrumentationTest)
}