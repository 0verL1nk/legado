package top.overlink.read.ui.rss.read

import top.overlink.read.data.entities.BaseSource
import top.overlink.read.help.JsExtensions
import top.overlink.read.ui.association.AddToBookshelfDialog
import top.overlink.read.ui.book.search.SearchActivity
import top.overlink.read.utils.showDialogFragment

@Suppress("unused")
class RssJsExtensions(private val activity: ReadRssActivity) : JsExtensions {

    override fun getSource(): BaseSource? {
        return activity.getSource()
    }

    fun searchBook(key: String) {
        SearchActivity.start(activity, key)
    }

    fun addBook(bookUrl: String) {
        activity.showDialogFragment(AddToBookshelfDialog(bookUrl))
    }

}
