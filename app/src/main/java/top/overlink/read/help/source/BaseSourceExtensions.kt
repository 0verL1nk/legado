package top.overlink.read.help.source

import top.overlink.read.constant.SourceType
import top.overlink.read.data.entities.BaseSource
import top.overlink.read.data.entities.BookSource
import top.overlink.read.data.entities.RssSource
import top.overlink.read.model.SharedJsScope
import org.mozilla.javascript.Scriptable
import kotlin.coroutines.CoroutineContext

fun BaseSource.getShareScope(coroutineContext: CoroutineContext? = null): Scriptable? {
    return SharedJsScope.getScope(jsLib, coroutineContext)
}

fun BaseSource.getSourceType(): Int {
    return when (this) {
        is BookSource -> SourceType.book
        is RssSource -> SourceType.rss
        else -> error("unknown source type: ${this::class.simpleName}.")
    }
}
