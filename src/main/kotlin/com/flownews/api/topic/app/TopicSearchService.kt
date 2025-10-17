package com.flownews.api.topic.app

import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.infra.CustomOAuth2User
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class TopicSearchService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
) {
    fun search(
        principal: CustomOAuth2User?,
        query: String,
        limit: Int,
        page: Int,
    ): List<TopicSummaryResponse> {
        val pageable = buildPageable(limit, page)
        val keyword = query.trim()
        val topics =
            topicRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword,
                keyword,
                pageable,
            )

        val user = principal?.getUser()
        val userId = user?.requireId() ?: return topics.map(TopicSummaryResponse::fromEntity)

        return topics.map { topic ->
            val isFollowing = topicSubscriptionRepository.existsByTopicIdAndUserId(topic.requireId(), userId)
            TopicSummaryResponse.fromEntity(topic, isFollowing)
        }
    }

    private fun buildPageable(
        limit: Int,
        page: Int,
    ): Pageable {
        val safeLimit = limit.coerceIn(MIN_LIMIT, MAX_LIMIT)
        val safePage = page.coerceAtLeast(0)
        return PageRequest.of(safePage, safeLimit, Sort.by("createdAt").descending())
    }

    companion object {
        private const val MIN_LIMIT = 1
        private const val MAX_LIMIT = 50
    }
}
