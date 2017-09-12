package cz.mapofnews.testconfig

import cz.mapofnews.android.MainActivity
import cz.mapofnews.android.MapViewFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TestAppModule {

    @ContributesAndroidInjector
    abstract fun mainActivityInjector(): MainActivity

    @ContributesAndroidInjector
    abstract fun mapViewFragmentInjector(): MapViewFragment

}