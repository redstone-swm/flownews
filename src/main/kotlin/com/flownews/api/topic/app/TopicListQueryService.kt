package com.flownews.api.topic.app

import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TopicListQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
) {
    fun getTopics(
        user: User?,
        limit: Int,
    ): List<TopicSummaryResponse> {
        val pageRequest = getPageRequest(limit)
        val topics = topicRepository.findAll(pageRequest)

        if (user == null) {
            return topics.map(TopicSummaryResponse::fromEntity)
        }

        val userId = user.requireId()
        return topics.map { topic ->
            val isFollowing = topicSubscriptionRepository.existsByTopicIdAndUserId(topic.requireId(), userId)
            TopicSummaryResponse.fromEntity(topic, isFollowing)
        }
    }

    private fun getPageRequest(limit: Int) = PageRequest.of(0, limit, Sort.by("createdAt").descending())

    fun getTopKTopicsInLast24Hours(limit: Int): List<TopicTopKQueryResponse> {
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24)
        val topics = topicRepository.findTopKTopicsByInteractionsSince(twentyFourHoursAgo, null, limit)

        return topics.map { it -> TopicTopKQueryResponse(it.requireId(), it.title) }
    }
}
