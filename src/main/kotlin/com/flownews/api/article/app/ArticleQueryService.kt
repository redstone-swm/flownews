package com.flownews.api.article.app

import com.flownews.api.article.domain.ArticleRepository
import com.flownews.api.common.app.NoDataException
import org.springframework.stereotype.Service

@Service
class ArticleQueryService(
    private val articleRepository: ArticleRepository
) {
    fun getArticleById(articleId: String): ArticleQueryResponse =
        articleRepository.findByArticleId(articleId).map { it ->
            ArticleQueryResponse(it.articleId, it.title, it.body)
        }.orElseThrow { NoDataException() }
}