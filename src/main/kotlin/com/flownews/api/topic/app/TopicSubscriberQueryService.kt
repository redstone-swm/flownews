package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import org.springframework.stereotype.Service

@Service
class TopicSubscriberQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository
) {
    fun getTopicWithSubscribers(id: Long): TopicWithSubscribers {
        val topic = topicRepository.findById(id).orElseThrow { NoDataException("topic not found : $id") }
        val subscriptions = topicSubscriptionRepository.findByTopicId(id)
        val subscribers = subscriptions.map { it.visitor }

        return TopicWithSubscribers(topic, subscribers)
    }
}