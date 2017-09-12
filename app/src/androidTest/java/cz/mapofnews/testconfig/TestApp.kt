package cz.mapofnews.testconfig

import cz.mapofnews.config.App

class TestApp : App() {

    lateinit var component: TestAppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerTestAppComponent.create()
        component.inject(this)
    }

}