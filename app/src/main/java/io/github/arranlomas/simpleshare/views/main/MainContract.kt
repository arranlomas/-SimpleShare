package io.github.arranlomas.simpleshare.views.main

import com.arranlomas.kontent.commons.objects.KontentContract

/**
 * Created by arran on 15/04/2018.
 */
interface TorrentInfoContract {
    interface ViewModel : KontentContract.ViewModel<MainActions, MainResults, MainViewState>
}