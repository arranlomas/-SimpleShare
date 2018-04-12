package io.github.arranlomas.simpleshare

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import com.schiwfty.torrentwrapper.utils.getMagnetLink
import com.schiwfty.torrentwrapper.utils.openFile
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.exceptions.CompositeException
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

/**
 * Created by arran on 10/04/2018.
 */

val TEST_FILE = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path, "test3.jpg")
val magnet = "magnet:?xt=urn:btih:81c814fe5901f2f6ff50113258ed06344196c733&dn=test3.jpg&tr=udp://tracker.coppersurfer.tk:6969/announce&tr=udp://tracker.skyts.net:6969/announce&tr=udp://tracker.safe.moe:6969/announce&tr=udp://tracker.piratepublic.com:1337/announce&tr=udp://tracker.pirateparty.gr:6969/announce&tr=udp://allesanddro.de:1337/announce&tr=udp://9.rarbg.com:2710/announce&tr=udp://p4p.arenabg.com:1337/announce&tr=http://p4p.arenabg.com:1337/announce&tr=udp://tracker.opentrackr.org:1337/announce&tr=http://tracker.opentrackr.org:1337/announce&tr=http://asnet.pw:2710/announce&tr=udp://tracker.internetwarriors.net:1337/announce&tr=udp://public.popcorn-tracker.org:6969/announce&tr=udp://tracker1.wasabii.com.tw:6969/announce&tr=udp://tracker.zer0day.to:1337/announce&tr=udp://tracker.mg64.net:6969/announce&tr=udp://peerfect.org:6969/announce&tr=http://tracker.mg64.net:6881/announce&tr=http://mgtracker.org:6969/announce"

class MainActivity : AppCompatActivity() {
    lateinit var torrentRepository: ITorrentRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + getString(R.string.app_name)
        Confluence.install(applicationContext, directoryPath, 8081)
        Confluence.start(this, R.drawable.ic_android_black_24dp, "channelId", "channelName", true, true, null)
                .map { Log.v("confluence state", it.name) }
                .flatMap { torrentRepository.isConnected(); }
                .subscribe {
                    Log.v("is connected", "$it")
                }

        torrentRepository = Confluence.torrentRepository
        RxPermissions(this).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    addFile.setOnClickListener {
                        updloadFile(TEST_FILE)
                    }

                    downloadFile.setOnClickListener {
                        downloadFile(magnet)
                    }
                }
    }

    fun updloadFile(file: File) {
        torrentRepository.addFileToClient(file)
                .flatMap { torrentInfo -> torrentRepository.getStatus().doOnNext { status.text = it }.map { torrentInfo } }
                .subscribe({
                    Log.v("Torrent Added", it.getMagnetLink())
                }, {
                    if (it is CompositeException){
                        it.exceptions.forEach {
                            it.printStackTrace()
                            Log.e("Error adding file", it.localizedMessage)
                        }
                    }
                    it.printStackTrace()
                    Log.e("Error adding file", it.localizedMessage)
                })
    }

    fun downloadFile(magnet: String) {
        val trackers = magnet.findTrackersFromMagnet()
        val hash = magnet.findHashFromMagnet()
        torrentRepository.downloadTorrentInfo(hash!!, trackers = trackers)
                .flatMap { torrentInfo -> torrentRepository.getStatus().doOnNext { status.text = it }.map { torrentInfo } }
                .subscribe({
                    it.unwrapIfSuccess {
                        torrentRepository.startFileDownloading(it.fileList.first(), this, true)
//                        torrentRepository.startFileDownloading(it.fileList.first(), this, true)
                        Log.v("Torrent info downloaded", it.info_hash)
                    } ?: Log.v("Parse torrent result", "Failed")
                }, {
                    it.printStackTrace()
                    Log.e("Error downloading info", it.localizedMessage)
                })
    }
}