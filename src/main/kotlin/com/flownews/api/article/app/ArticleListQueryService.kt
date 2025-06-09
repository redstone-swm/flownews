package com.flownews.api.article.app

import com.flownews.api.article.domain.ArticleRepository
import org.springframework.stereotype.Service

@Service
class ArticleListQueryService(
    private val articleRepository: ArticleRepository
) {
    fun getAllArticles(): List<ArticleListQueryResponse> =
        articleRepository.findAll().map { it -> ArticleListQueryResponse(it.articleId, it.title) }
}