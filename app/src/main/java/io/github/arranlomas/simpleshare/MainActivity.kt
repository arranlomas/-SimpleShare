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
import com.tbruyelle.rxpermissions2.RxPermissions
import droidninja.filepicker.FilePickerBuilder
import io.reactivex.exceptions.CompositeException
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import droidninja.filepicker.FilePickerConst
import android.app.Activity
import android.content.Intent
import android.widget.Toast


/**
 * Created by arran on 10/04/2018.
 */
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
                        FilePickerBuilder.getInstance().setMaxCount(1)
                                .setActivityTheme(R.style.LibAppTheme)
                                .pickPhoto(this)
                    }

                    downloadFile.setOnClickListener {
                        //TODO add dialog to add magnet
                    }
                }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FilePickerConst.REQUEST_CODE_PHOTO -> if (resultCode == Activity.RESULT_OK && data != null) {
                val paths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).toList()
                paths.firstOrNull()?.let {
                    torrentRepository.updloadFile(File(it), { hash, torrentInfo ->
                        val magnet = torrentInfo.getMagnetLink()
                        magnet.copyToClipboard(this, "magnet")
                        Toast.makeText(this, "link copied to clipboard", Toast.LENGTH_LONG).show()
                    })
                }
            }
        }
    }
}