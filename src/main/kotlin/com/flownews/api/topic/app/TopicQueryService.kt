package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicHistoryRepository
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.app.CustomOAuth2User
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class TopicQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val topicHistoryRepository: TopicHistoryRepository,
) {
    fun getTopic(
        id: Long,
        user: CustomOAuth2User?,
    ): TopicDetailsResponse {
        val topic = topicRepository.findById(id).orElseThrow { NoDataException("topic not found : $id") }
        val randomTopics = getRandomTopics(id)

        if (user == null) {
            return TopicDetailsResponse.fromEntity(topic, randomTopics)
        }

        val userId = user.getUser().requireId()
        val topicHistory = topicHistoryRepository.findByTopicIdAndUserId(id, userId)

        return TopicDetailsResponse.fromEntity(topic, randomTopics, topicHistory)
    }

    fun getTopicWithSubscribers(id: Long): TopicWithSubscribers {
        val topic = topicRepository.findById(id).orElseThrow { NoDataException("topic not found : $id") }
        val subscriptions = topicSubscriptionRepository.findByTopicId(id)
        val subscribers = subscriptions.map { it.user }

        return TopicWithSubscribers(topic, subscribers)
    }

    private fun getRandomTopics(
        excludeTopicId: Long,
        limit: Int = 3,
    ): List<Topic> {
        val topics = topicRepository.findAllExceptOneRandom(excludeTopicId, PageRequest.of(0, limit))
        if (topics.isEmpty()) {
            throw NoDataException("No topics found")
        }
        return topics
    }
}
