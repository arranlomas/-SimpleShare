package io.github.arranlomas.simpleshare

import android.arch.lifecycle.ViewModel
import com.arranlomas.daggerviewmodelhelper.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.github.arranlomas.simpleshare.di.ViewModelFactoryModule

/**
 * Created by arran on 15/02/2017.
 */
@Module(includes = arrayOf(ViewModelFactoryModule::class))
abstract class MainActivityModule {
    @ContributesAndroidInjector
    internal abstract fun bindsMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}
