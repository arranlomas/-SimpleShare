package io.github.arranlomas.simpleshare.base

import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.arranlomas.kontent_android_viewmodel.commons.objects.KontentAndroidViewModel
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

abstract class BaseMviViewModel<A : KontentAction, R : KontentResult, S : KontentViewState>(
        actionProcessor: ObservableTransformer<A, R>,
        defaultState: S,
        reducer: BiFunction<S, R, S>,
        postProcessor: (Function1<S, S>)? = null) :
        KontentAndroidViewModel<A, R, S>(actionProcessor, defaultState, reducer, postProcessor)