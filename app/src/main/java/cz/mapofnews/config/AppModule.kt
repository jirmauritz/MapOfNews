package cz.mapofnews.config

import cz.mapofnews.android.MainActivity
import cz.mapofnews.android.MapViewFragment
import cz.mapofnews.api.RetrieveApi
import cz.mapofnews.backendless.RetrieveApiBackendless
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton


@Module
abstract class AppModule {

    /**
     * Thanks to this method, the [MainActivity] can be injected very simply by AndroidInjector and
     * does not have to have component nor module instance.
     */
    @ContributesAndroidInjector
    abstract fun mainActivityInjector(): MainActivity

    /**
     * Thanks to this method, the [MapViewFragment] can be injected very simply by AndroidInjector and
     * does not have to have component nor module instance.
     */
    @ContributesAndroidInjector
    abstract fun mapViewFragmentInjector(): MapViewFragment

    /**
     * This signature says that the application keeps one instance of [RetrieveApi] here in application
     * module and specifies the implementing class. If we'd like to change data provider from Backendless
     * to som other, this is only place to change code.
     */
    @Binds
    @Singleton
    abstract fun bindRetrieveApi(retrieveApiBackendless: RetrieveApiBackendless): RetrieveApi

}