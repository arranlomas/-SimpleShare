package io.github.arranlomas.simpleshare.di

import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideTorrentRepository(): ITorrentRepository {
        return Confluence.torrentRepository
    }
}