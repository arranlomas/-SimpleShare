package io.github.arranlomas.simpleshare.views

import android.support.v7.widget.RecyclerView
import android.view.View
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.utils.formatBytesAsSize
import io.github.arranlomas.simpleshare.R
import kotlinx.android.synthetic.main.list_item_torrent.view.*

/**
 * Created by arran on 15/04/2018.
 */
class AllTorrentsCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var torrentInfo: TorrentInfo

    fun bind(torrentInfo: TorrentInfo) {
        this.torrentInfo = torrentInfo
        itemView.torrentInfoName.text = torrentInfo.name
        itemView.torrentInfoSize.text = torrentInfo.totalSize.formatBytesAsSize()
        if (torrentInfo.singleFileTorrent)
            itemView.torrentInfoFileCount.text = itemView.context.getString(R.string.one_file)
        else
            itemView.torrentInfoFileCount.text = itemView.context.getString(R.string.x_files, torrentInfo.fileList.size)
    }
}