package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import org.springframework.stereotype.Service

@Service
class TopicQueryService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val reactionRepository: ReactionRepository,
) {
//    fun getTopic(
//        id: Long,
//        user: CustomOAuth2User?,
//    ): TopicDetailsResponse {
//        val topic = topicRepository.findById(id).orElseThrow { NoDataException("topic not found : $id") }
//        val randomTopics = getRandomTopics(id)
//
//        if (user == null) {
//            val recommendTopics = randomTopics.map(TopicSummaryResponse::fromEntity)
//            return TopicDetailsResponse.fromEntity(topic, recommendTopics, reactionRepository)
//        }
//
//        val userId = user.getUser().requireId()
//        val topicHistory = topicHistoryRepository.findByTopicIdAndUserId(id, userId)
//        val isFollowing = topicSubscriptionRepository.existsByTopicIdAndUserId(id, userId)
//
//        val recommendTopics = randomTopics.map { recommendTopic ->
//            val isRecommendFollowing = topicSubscriptionRepository.existsByTopicIdAndUserId(recommendTopic.requireId(), userId)
//            TopicSummaryResponse.fromEntity(recommendTopic, isRecommendFollowing)
//        }
//
//        return TopicDetailsResponse.fromEntity(topic, recommendTopics, topicHistory, reactionRepository, isFollowing)
//    }

    fun getTopicWithSubscribers(id: Long): TopicWithSubscribers {
        val topic = topicRepository.findById(id).orElseThrow { NoDataException("topic not found : $id") }
        val subscriptions = topicSubscriptionRepository.findByTopicId(id)
        val subscribers = subscriptions.map { it.user }

        return TopicWithSubscribers(topic, subscribers)
    }
}
