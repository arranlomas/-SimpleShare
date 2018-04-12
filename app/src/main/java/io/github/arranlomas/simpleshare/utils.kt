package io.github.arranlomas.simpleshare

import com.schiwfty.torrentwrapper.models.TorrentInfo
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import java.io.File
import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE



/**
 * Created by arran on 12/04/2018.
 */
fun ITorrentRepository.downloadFile(magnet: String, onDownloaded: (String, TorrentInfo) -> Unit) {
    val trackers = magnet.findTrackersFromMagnet()
    val hash = magnet.findHashFromMagnet()
    downloadTorrentInfo(hash!!, trackers = trackers)
            .flatMap { torrentInfo ->
                getStatus()
                        .map { torrentInfo to it }
            }
            .subscribe({
                val (torrent, hash) = it
                torrent.unwrapIfSuccess {
                    onDownloaded.invoke(hash, it)
                }
            }, {
            })
}

fun ITorrentRepository.updloadFile(file: File, onDownloaded: (String, TorrentInfo) -> Unit) {
    addFileToClient(file)
            .flatMap { torrentInfo ->
                getStatus()
                        .map { torrentInfo to it }
            }
            .subscribe({
                val (torrent, hash) = it
                onDownloaded.invoke(hash, torrent)
            }, {
            })
}

fun String.copyToClipboard(context: Context, label: String){
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, this)
    clipboard.primaryClip = clip
}