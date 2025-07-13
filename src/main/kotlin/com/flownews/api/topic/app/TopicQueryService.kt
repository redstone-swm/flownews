package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class TopicQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository
) {

    fun getTopic(id: Long): TopicDetailsResponse {
        val topic = topicRepository.findById(id).orElseThrow { NoDataException("topic not found : $id") }

        return TopicDetailsResponse.fromEntity(topic)
    }

    fun getRandomTopic(): TopicDetailsResponse {
        val topics = topicRepository.findAllByRandom(PageRequest.of(0, 1))
        if (topics.isEmpty()) {
            throw NoDataException("No topic found")
        }
        return TopicDetailsResponse.fromEntity(topics[0])
    }

    fun getTopicWithSubscribers(id: Long): TopicWithSubscribers {
        val topic = topicRepository.findById(id).orElseThrow { NoDataException("topic not found : $id") }
        val subscriptions = topicSubscriptionRepository.findByTopicId(id)
        val subscribers = subscriptions.map { it.user }

        return TopicWithSubscribers(topic, subscribers)
    }
}