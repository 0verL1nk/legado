package top.overlink.read.ui.book.manga.entities

import top.overlink.read.data.entities.BookChapter

data class MangaChapter(
    val chapter: BookChapter,
    val pages: List<BaseMangaPage>,
    val imageCount: Int
)
