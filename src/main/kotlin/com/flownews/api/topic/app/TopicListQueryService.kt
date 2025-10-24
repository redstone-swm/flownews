package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
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

    fun getTopKTopics(limit: Int): List<TopicTopKQueryResponse> =
        findTopTopicsSinceLast24Hours(limit).map { TopicTopKQueryResponse(it.requireId(), it.title) }

    fun findTopTopicsSinceLast24Hours(limit: Int): List<Topic> {
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24).toLocalDate()
        return topicRepository.findTopKTopicsByInteractionsSince(twentyFourHoursAgo, null, limit)
    }
}
