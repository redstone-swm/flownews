package com.flownews.api.article.domain

import org.springframework.data.annotation.Id
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document(collection = "articles")
data class Article(
    @Field("article_id")
    val articleId: String,
    val body: String,
    //val date: LocalDateTime,
    val rank: Int,
    val title: String,
    val url: String
)