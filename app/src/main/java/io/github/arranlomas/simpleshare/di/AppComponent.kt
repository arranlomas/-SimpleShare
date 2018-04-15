package io.github.arranlomas.simpleshare.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import io.github.arranlomas.simpleshare.Application
import io.github.arranlomas.simpleshare.views.main.MainActivityModule
import javax.inject.Singleton

/**
 * Created by arran on 8/10/2017.
 */
@Singleton
@Component(modules = arrayOf(
        AndroidSupportInjectionModule::class,
        MainActivityModule::class,
        RepositoryModule::class))
interface AppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: Application)
}