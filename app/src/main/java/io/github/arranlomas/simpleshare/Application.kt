package io.github.arranlomas.simpleshare

import android.content.Context
import android.support.multidex.MultiDex
import com.arranlomas.daggerviewmodelhelper.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.github.arranlomas.simpleshare.di.DaggerAppComponent


/**x
 * Created by arran on 29/04/2017.
 */
class Application : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        AppInjector.init(this)
        val appComponent = DaggerAppComponent.create()
        appComponent.inject(this)
        return appComponent
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}