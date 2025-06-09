package com.flownews.api.article.domain

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ArticleRepository : MongoRepository<Article, String> {
    fun findByArticleId(articleId: String): Optional<Article>
}