package io.github.arranlomas.simpleshare

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.schiwfty.torrentwrapper.confluence.Confluence
import com.schiwfty.torrentwrapper.repositories.ITorrentRepository
import com.schiwfty.torrentwrapper.utils.findHashFromMagnet
import com.schiwfty.torrentwrapper.utils.findTrackersFromMagnet
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

/**
 * Created by arran on 10/04/2018.
 */

val TEST_FILE = File(Environment.DIRECTORY_DOWNLOADS, "test.jpeg")
val TEST_MAGNET = "magnet"

class MainActivity : AppCompatActivity() {
    lateinit var torrentRepository: ITorrentRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + getString(R.string.app_name)
        Confluence.install(applicationContext, directoryPath, 8081)
        torrentRepository = Confluence.torrentRepository
        addFile.setOnClickListener {
            updloadFile(TEST_FILE)
        }

        downloadFile.setOnClickListener {
            downloadFile(TEST_MAGNET)
        }
    }

    fun updloadFile(file: File) {
        torrentRepository.addFileToClient(file)
                .subscribe({
                    Log.v("Torrent Added", it.info_hash)
                }, {
                    it.printStackTrace()
                    Log.e("Error adding file", it.localizedMessage)
                })
    }

    fun downloadFile(magnet: String) {
        val trackers = magnet.findTrackersFromMagnet()
        val hash = magnet.findHashFromMagnet()
        torrentRepository.downloadTorrentInfo(hash!!, trackers = trackers)
                .subscribe({
                    it.unwrapIfSuccess {
                        Log.v("Torrent info downloaded", it.info_hash)
                    } ?: Log.v("Parse torrent result", "Failed")
                }, {
                    it.printStackTrace()
                    Log.e("Error downloading info", it.localizedMessage)
                })
    }
}