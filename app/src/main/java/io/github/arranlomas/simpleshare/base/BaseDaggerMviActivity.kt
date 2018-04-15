package io.github.arranlomas.simpleshare.base

import android.arch.lifecycle.ViewModelProvider
import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kotentdaggersupport.KontentDaggerSupportActivity
import javax.inject.Inject

/**
 * Created by arran on 11/07/2017.
 */
abstract class BaseDaggerMviActivity<A : KontentAction, R : KontentResult, S : KontentViewState> : KontentDaggerSupportActivity<A, R, S>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
}