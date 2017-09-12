package cz.mapofnews.config

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Component of the application, all instances that wants to be singleton should have its module here and provide
 * method in [AppModule].
 */
@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AppModule::class))
interface AppComponent : AndroidInjector<App>