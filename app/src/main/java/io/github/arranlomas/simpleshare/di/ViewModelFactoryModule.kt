package io.github.arranlomas.simpleshare.di

import android.arch.lifecycle.ViewModelProvider
import com.arranlomas.daggerviewmodelhelper.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Created by arran on 8/03/2018.
 */

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}


