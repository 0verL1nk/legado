package top.overlink.read.ui.rss.article

import android.content.Context
import androidx.viewbinding.ViewBinding
import top.overlink.read.base.adapter.RecyclerAdapter
import top.overlink.read.data.entities.RssArticle


abstract class BaseRssArticlesAdapter<VB : ViewBinding>(context: Context, val callBack: CallBack) :
    RecyclerAdapter<RssArticle, VB>(context) {

    interface CallBack {
        val isGridLayout: Boolean
        fun readRss(rssArticle: RssArticle)
    }
}