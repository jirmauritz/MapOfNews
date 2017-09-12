package cz.mapofnews.testconfig

import com.nhaarman.mockito_kotlin.mock
import cz.mapofnews.api.RetrieveApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRetrieveApiModule {

    @Provides
    @Singleton
    fun provideRetrieveApi(): RetrieveApi {
        return mock()
    }
}