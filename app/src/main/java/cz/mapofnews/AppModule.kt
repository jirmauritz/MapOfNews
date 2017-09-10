package cz.mapofnews

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

    @ContributesAndroidInjector
    abstract fun mainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    abstract fun mapViewFragmentInjector(): MapViewFragment

    @Binds
    @Singleton
    abstract fun bindRetrieveApi(retrieveApiBackendless: RetrieveApiBackendless): RetrieveApi

}