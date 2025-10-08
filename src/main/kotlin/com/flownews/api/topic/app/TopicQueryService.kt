package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service

@Service
class TopicQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val reactionRepository: ReactionRepository,
) {
    fun getTopic(
        user: User,
        topicId: Long,
    ): TopicQueryResponse {
        val topic = getTopic(topicId)
        val userId = user.requireId()

        val isFollowing = topicSubscriptionRepository.existsByTopicIdAndUserId(topicId, userId)

        return TopicQueryResponse.of(
            topic = topic,
            isFollowing = isFollowing,
            reactionRepository = reactionRepository,
            topicSubscriptionRepository = topicSubscriptionRepository,
            user = user,
        )
    }

    fun getTopicWithSubscribers(topicId: Long): TopicWithSubscribers {
        val topic = getTopic(topicId)
        val subscriptions = topicSubscriptionRepository.findByTopicId(topicId)
        val subscribers = subscriptions.map { it.user }

        return TopicWithSubscribers(topic, subscribers)
    }

    private fun getTopic(topicId: Long): Topic =
        topicRepository
            .findById(topicId)
            .orElseThrow { NoDataException("Topic not found : $topicId") }
}
