package com.flownews.api.article.api


import com.flownews.api.article.app.ArticleQueryService
import com.flownews.api.common.api.ApiResponse
import com.flownews.api.common.app.NoDataException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleQueryApi(
    private val articleQueryService: ArticleQueryService
) {

    @GetMapping("/api/articles/{articleId}")
    fun getArticleById(@PathVariable articleId: String): ApiResponse<out Any?> {
        return try {
            ApiResponse.ok(articleQueryService.getArticleById(articleId))
        } catch (e: NoDataException) {
            ApiResponse.badRequest(e.message)
        }
    }
}