package com.flownews.api.topic.domain

import com.flownews.api.common.app.NoDataException
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class TopicQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
) {
    fun getTopicWithSubscribers(topicId: Long): TopicWithSubscribers {
        val topic = findTopic(topicId)
        val subscriptions = topicSubscriptionRepository.findByTopicId(topicId)
        val subscribers = subscriptions.map { it.user }

        return TopicWithSubscribers(topic, subscribers)
    }

    fun getFollowedTopic(
        user: User?,
        topicId: Long,
    ): FollowedTopic {
        val topic = findTopic(topicId)
        val isFollowing =
            user?.let {
                topicSubscriptionRepository.existsByTopicIdAndUserId(topicId, it.requireId())
            } ?: false
        return FollowedTopic(topic, isFollowing)
    }

    private fun findTopic(topicId: Long): Topic =
        topicRepository
            .findById(topicId)
            .orElseThrow { NoDataException("Topic not found : $topicId") }
}
