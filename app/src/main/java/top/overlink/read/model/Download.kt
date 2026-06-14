package top.overlink.read.model

import android.content.Context
import top.overlink.read.constant.IntentAction
import top.overlink.read.service.DownloadService
import top.overlink.read.utils.startService

object Download {


    fun start(context: Context, url: String, fileName: String) {
        context.startService<DownloadService> {
            action = IntentAction.start
            putExtra("url", url)
            putExtra("fileName", fileName)
        }
    }

}