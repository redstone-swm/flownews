package com.flownews.api.topic.app

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

data class TopicListQueryRequest(
    val query: String?,
    val limit: Int = 20,
    val page: Int = 0,
) {
    companion object {
        private const val MIN_LIMIT = 1
        private const val MAX_LIMIT = 50
    }

    fun getKeyword(): String {
        return query?.trim() ?: throw IllegalArgumentException("Query parameter is required")
    }

    fun toPageable(): Pageable {
        val safeLimit = limit.coerceIn(MIN_LIMIT, MAX_LIMIT)
        val safePage = page.coerceAtLeast(0)
        return PageRequest.of(safePage, safeLimit, Sort.by("createdAt").descending())
    }
}
