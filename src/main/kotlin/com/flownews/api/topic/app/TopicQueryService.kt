package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.event.app.EventQueryService
import com.flownews.api.topic.domain.FollowedTopic
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class TopicQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val eventQueryService: EventQueryService,
) {
    fun getTopic(
        user: User?,
        topicId: Long,
    ): TopicQueryResponse {
        val followedTopic = getFollowedTopic(user, topicId)
        val reactedEvents =
            followedTopic.topic.getEvents().map {
                eventQueryService.getReactedEvent(it.requireId(), user)
            }

        return TopicQueryResponse.of(
            followedTopic = followedTopic,
            events = reactedEvents,
        )
    }

    fun getTopicWithSubscribers(topicId: Long): TopicWithSubscribers {
        val topic = findTopic(topicId)
        val subscriptions = topicSubscriptionRepository.findByTopicId(topicId)
        val subscribers = subscriptions.map { it.user }

        return TopicWithSubscribers(topic, subscribers)
    }

    private fun getFollowedTopic(
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
