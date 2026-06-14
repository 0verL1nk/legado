package top.overlink.read.ui.main.explore

import android.app.Application
import top.overlink.read.base.BaseViewModel
import top.overlink.read.data.appDb
import top.overlink.read.data.entities.BookSourcePart
import top.overlink.read.help.config.SourceConfig
import top.overlink.read.help.source.SourceHelp

class ExploreViewModel(application: Application) : BaseViewModel(application) {

    fun topSource(bookSource: BookSourcePart) {
        execute {
            val minXh = appDb.bookSourceDao.minOrder
            bookSource.customOrder = minXh - 1
            appDb.bookSourceDao.upOrder(bookSource)
        }
    }

    fun deleteSource(source: BookSourcePart) {
        execute {
            SourceHelp.deleteBookSource(source.bookSourceUrl)
        }
    }

}