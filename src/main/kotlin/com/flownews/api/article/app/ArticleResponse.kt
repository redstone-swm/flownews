package com.flownews.api.article.app

import com.flownews.api.article.domain.Article
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "기사 정보")
data class ArticleResponse(
    @Schema(description = "기사 ID", example = "1")
    val id: Long,
    @Schema(description = "기사 제목", example = "손흥민 골")
    val title: String,
    @Schema(description = "기사 출처(언론사)", example = "연합뉴스")
    val source: String,
    @Schema(description = "기사 링크", example = "https://example.com/news/1")
    val url: String
) {
    companion object {
        fun fromEntity(article: Article) = ArticleResponse(
            id = article.id ?: throw IllegalStateException("Article ID cannot be null"),
            title = article.title,
            source = article.source,
            url = article.url
        )
    }
}
