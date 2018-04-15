package io.github.arranlomas.simpleshare

import com.arranlomas.kontent.commons.functions.KontentActionProcessor
import com.arranlomas.kontent.commons.functions.KontentMasterProcessor
import com.arranlomas.kontent.commons.functions.KontentReducer
import com.arranlomas.kontent.commons.objects.KontentAction
import com.arranlomas.kontent.commons.objects.KontentResult
import com.arranlomas.kontent.commons.objects.KontentViewState
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import io.github.arranlomas.simpleshare.base.BaseMviViewModel
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by arran on 15/04/2018.
 */
class MainViewModel @Inject constructor(torrentRepository: ITorrentRepository)
    : TorrentInfoContract.ViewModel, BaseMviViewModel<MainActions, MainResults, MainViewState>(
        actionProcessor = torrentInfoActionProcessor(torrentRepository),
        reducer = torrentInfoReducer,
        defaultState = MainViewState.default()

)

fun torrentInfoActionProcessor(torrentRepository: ITorrentRepository) = KontentMasterProcessor<MainActions, MainResults> { action ->
    Observable.merge(observables(action, torrentRepository))
}

private fun observables(shared: Observable<MainActions>, torrentRepository: ITorrentRepository): List<Observable<MainResults>> {
    return listOf<Observable<MainResults>>(
            shared.ofType(MainActions.Load::class.java).compose(loadTorrents(torrentRepository)))
}

fun loadTorrents(torrentRepository: ITorrentRepository) =
        KontentActionProcessor<MainActions.Load, MainResults, List<TorrentInfo>>(
                action = {
                    torrentRepository.getAllTorrentsFromStorage()
                            .map {
                                val results = mutableListOf<TorrentInfo>()
                                it.forEach {
                                    it.unwrapIfSuccess {
                                        results.add(it)
                                    }
                                }
                                results.toList()
                            }
                },
                success = {
                    MainResults.LoadSuccess(it)
                },
                error = {
                    MainResults.LoadError(it)
                },
                loading = MainResults.LoadInFlight()
        )

val torrentInfoReducer = KontentReducer { result: MainResults, previousState: MainViewState ->
    when (result) {
        is MainResults.LoadSuccess -> previousState.copy(isLoading = false, error = null, torrents = result.results)
        is MainResults.LoadError -> previousState.copy(isLoading = false, error = result.error.localizedMessage)
        is MainResults.LoadInFlight -> previousState.copy(isLoading = true, error = null)
    }
}

sealed class MainActions : KontentAction() {
    class Load : MainActions()
}

sealed class MainResults : KontentResult() {
    data class LoadSuccess(val results: List<TorrentInfo>) : MainResults()
    data class LoadError(val error: Throwable) : MainResults()
    class LoadInFlight : MainResults()
}

data class MainViewState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val torrents: List<TorrentInfo>? = null
) : KontentViewState() {
    companion object Factory {
        fun default(): MainViewState {
            return MainViewState()
        }
    }
}