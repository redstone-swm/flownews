package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSubscription
import com.flownews.api.topic.domain.TopicSubscriptionId
import com.flownews.api.topic.domain.TopicSubscriptionRepository
import com.flownews.api.user.app.UserUpdateService
import com.flownews.api.user.domain.User
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TopicSubscribeService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val userUpdateService: UserUpdateService,
) {
    @Transactional
    fun subscribeTopic(req: TopicSubscribeRequest) {
        val topic = getTopic(req.topicId)
        val user = req.user

        val userId = user.requireId()
        val topicId = topic.requireId()
        val deviceToken = req.deviceToken ?: throw IllegalStateException("User device token cannot be null")
        val subscriptionId = TopicSubscriptionId(userId, topicId)

        if (topicSubscriptionRepository.existsById(subscriptionId)) {
            throw IllegalStateException("이미 구독한 토픽입니다")
        }

        userUpdateService.updateDeviceToken(userId, deviceToken)
        saveSubscription(user, topic)
    }

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
