package com.flownews.api.article.api


import com.flownews.api.article.app.ArticleListQueryResponse
import com.flownews.api.article.app.ArticleListQueryService
import com.flownews.api.common.api.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleListQueryApi(
    private val articleListQueryService: ArticleListQueryService
) {

    @GetMapping("/articles")
    fun getAllArticles(): ApiResponse<List<ArticleListQueryResponse>> {
        return ApiResponse.ok(articleListQueryService.getAllArticles())
    }
}