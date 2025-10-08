package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscription
import com.flownews.api.topic.domain.TopicSubscriptionId
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.domain.User
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TopicSubscribeService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
) {
    @Transactional
    fun subscribeTopic(req: TopicSubscribeRequest) {
        val (user, topicId) = req
        val topic = getTopic(topicId)

        val userId = user.requireId()
        val subscriptionId = TopicSubscriptionId(userId, topicId)

        if (topicSubscriptionRepository.existsById(subscriptionId)) {
            throw IllegalStateException("이미 구독한 토픽입니다")
        }

        saveSubscription(user, topic)

        // TODO 토픽 구독시 인터렉션 로직 추가
    }

    @Transactional
    fun subscribeTopics(req: TopicMultipleSubscribeRequest) =
        req.topicId.forEach { topicId -> subscribeTopic(TopicSubscribeRequest(req.user, topicId)) }

    @Transactional
    fun unsubscribeTopic(req: TopicSubscribeRequest) {
        val topic = getTopic(req.topicId)
        val user = req.user

        val userId = user.requireId()
        val topicId = topic.requireId()

        val subscription =
            topicSubscriptionRepository.findByTopicIdAndUserId(topicId, userId)
                ?: throw NoDataException("No subscription found for user $userId and topic $topicId")

        topicSubscriptionRepository.delete(subscription)
    }

    @Transactional
    fun toggleSubscription(req: TopicSubscribeRequest): TopicSubscriptionToggleResponse {
        val topic = getTopic(req.topicId)
        val user = req.user

        val userId = user.requireId()
        val topicId = topic.requireId()

        val existingSubscription = topicSubscriptionRepository.findByTopicIdAndUserId(topicId, userId)

        return if (existingSubscription != null) {
            // 구독이 있으면 해제
            topicSubscriptionRepository.delete(existingSubscription)
            TopicSubscriptionToggleResponse(
                isSubscribed = false,
                message = "토픽 구독을 해제했습니다."
            )
        } else {
            // 구독이 없으면 구독
            saveSubscription(user, topic)
            TopicSubscriptionToggleResponse(
                isSubscribed = true,
                message = "토픽을 구독했습니다."
            )
        }
    }

    private fun saveSubscription(
        user: User,
        topic: Topic,
    ) {
        topicSubscriptionRepository.save(TopicSubscription(user = user, topic = topic))
    }

    private fun getTopic(topicId: Long): Topic =
        topicRepository
            .findById(topicId)
            .orElseThrow { NoDataException("Topic not found : $topicId") }
}
