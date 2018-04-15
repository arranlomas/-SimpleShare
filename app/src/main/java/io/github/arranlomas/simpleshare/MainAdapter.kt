package io.github.arranlomas.simpleshare

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.pawegio.kandroid.inflateLayout
import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.utils.onClick

/**
 * Created by arran on 15/04/2018.
 */
class MainAdapter(val itemClickListener: (TorrentInfo) -> Unit) : RecyclerView.Adapter<AllTorrentsCardHolder>() {
    var torrentFiles: List<TorrentInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllTorrentsCardHolder {
        val itemView = parent.context.inflateLayout(R.layout.list_item_torrent, parent, false)
        val holder = AllTorrentsCardHolder(itemView)
        holder.onClick { view, position, type ->
            itemClickListener.invoke(torrentFiles[position])
        }
        return holder
    }

    override fun onBindViewHolder(holder: AllTorrentsCardHolder, position: Int) {
        holder.bind(torrentFiles[position])
    }

    override fun getItemCount(): Int {
        return torrentFiles.size
    }
}