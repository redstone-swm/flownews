package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TopicListQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
) {
    fun getTopics(
        user: User?,
        req: TopicListQueryRequest,
    ): List<TopicSummaryResponse> {
        val pageRequest = req.toPageable()
        val topics = topicRepository.findAll(pageRequest)
        return mapToTopicSummaryResponses(topics, user)
    }

    fun getTopicsByKeyword(
        user: User?,
        req: TopicListQueryRequest,
    ): List<TopicSummaryResponse> {
        val pageable = req.toPageable()
        val keyword = req.getKeyword()
        val topics =
            topicRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword,
                keyword,
                pageable,
            )
        return mapToTopicSummaryResponses(topics, user)
    }

    private fun mapToTopicSummaryResponses(
        topics: List<Topic>,
        user: User?,
    ): List<TopicSummaryResponse> {
        if (user == null) {
            return topics.map(TopicSummaryResponse::fromEntity)
        }

        val userId = user.requireId()
        return topics.map { topic ->
            val isFollowing = topicSubscriptionRepository.existsByTopicIdAndUserId(topic.requireId(), userId)
            TopicSummaryResponse.fromEntity(topic, isFollowing)
        }
    }

    fun getTopKTopics(limit: Int): List<TopicTopKQueryResponse> =
        findTopTopicsSinceLast24Hours(limit).map { TopicTopKQueryResponse(it.requireId(), it.title) }

    fun findTopTopicsSinceLast24Hours(limit: Int): List<Topic> {
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24).toLocalDate()
        return topicRepository.findTopKTopicsByInteractionsSince(twentyFourHoursAgo, null, limit)
    }
}
