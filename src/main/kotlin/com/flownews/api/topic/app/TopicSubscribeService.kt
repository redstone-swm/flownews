package com.flownews.api.topic.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.domain.*
import com.flownews.api.user.app.UserUpdateService
import com.flownews.api.user.domain.User
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TopicSubscribeService(
    private val topicRepository: TopicRepository,
    private val topicSubscriptionRepository: TopicSubscriptionRepository,
    private val userUpdateService: UserUpdateService
) {

    @Transactional
    fun subscribeTopic(req: TopicSubscribeRequest) {
        val topic = getTopic(req.topicId)
        val user = req.user
        val subscriptionId = TopicSubscriptionId(user.id!!, topic.id!!)

        if (topicSubscriptionRepository.existsById(subscriptionId)) {
            throw IllegalStateException("이미 구독한 토픽입니다")
        }

        userUpdateService.updateDeviceToken(user.id!!, req.deviceToken!!)

        saveSubscription(user, topic)
    }

    private fun saveSubscription(user: User, topic: Topic) {
        topicSubscriptionRepository.save(TopicSubscription(user = user, topic = topic))
    }

    private fun getTopic(topicId: Long): Topic {
        return topicRepository.findById(topicId)
            .orElseThrow { NoDataException("Topic not found : $topicId") }
    }
}