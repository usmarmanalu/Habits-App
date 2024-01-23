package com.dicoding.habitapp.ui.news

import com.dicoding.habitapp.data.response.*

object DataDummy {

    fun generateDummyNewsResponse(): List<ArticlesItem> {

        val items: MutableList<ArticlesItem> = arrayListOf()
            for (i in 0..100) {
                val news = ArticlesItem(
                    publishedAt = "02 Feb 2022 | 17:10",
                    author = "Usmar Manalu",
                    description = "lorem ipsum bla bla bla",
                    url = "https://lsp.bsi.ac.id/LSPBSI/asesi/asesi/sertifikasi"
                )
                items.add(news)
            }
        return items
    }
}