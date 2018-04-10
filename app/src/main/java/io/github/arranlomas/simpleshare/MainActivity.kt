package io.github.arranlomas.simpleshare

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.schiwfty.torrentwrapper.confluence.Confluence
import java.io.File

/**
 * Created by arran on 10/04/2018.
 */
class MainActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + File.separator + getString(R.string.app_name)
        Confluence.install(applicationContext, directoryPath, 8081)
        val torrentRepository = Confluence.torrentRepository
    }
}