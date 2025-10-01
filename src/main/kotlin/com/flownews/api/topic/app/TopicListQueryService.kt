package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.infra.CustomOAuth2User
import org.springframework.stereotype.Service

@Service
class TopicListQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
) {
    fun getTopics(user: CustomOAuth2User?): List<TopicSummaryResponse> {
        val topics = topicRepository.findAll()

        if (user == null) {
            return topics.map(TopicSummaryResponse::fromEntity)
        }

        val userId = user.getUser().requireId()
        return topics.map { topic ->
            val isFollowing = topicSubscriptionRepository.existsByTopicIdAndUserId(topic.requireId(), userId)
            TopicSummaryResponse.fromEntity(topic, isFollowing)
        }
    }

    private fun getTopicByIds(ids: List<Long>): List<Topic> {
        val unordered = topicRepository.findAllById(ids).toList()
        val topicMap = unordered.associateBy { it.id }
        return ids.mapNotNull { topicMap[it] }
    }
}
