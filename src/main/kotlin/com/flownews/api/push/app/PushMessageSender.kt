package com.flownews.api.push.app

import com.flownews.api.push.domain.PushLog
import com.flownews.api.push.domain.PushLogRepository
import com.flownews.api.push.domain.PushMessage
import com.flownews.api.topic.app.TopicSubscriberQueryService
import com.flownews.api.topic.domain.Topic
import com.flownews.api.user.domain.Visitor
import org.springframework.stereotype.Service
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

@Service
class PushMessageSender(
    private val topicSubscriberQueryService: TopicSubscriberQueryService,
    private val pushLogRepository: PushLogRepository
) {

    fun sendPushMessages(topicId: Long) {
        val topicWithSubscribers = topicSubscriberQueryService.getTopicWithSubscribers(topicId)
        val topic = topicWithSubscribers.topic
        val subscribers = topicWithSubscribers.subscribers
        val messages = subscribers.map { createFrom(topic, it) }

        sendPushMessagesInternal(messages)
        appendPushLog(messages)
    }

    //FIXME Firebase 코드 이동 필요
    private fun sendPushMessagesInternal(messages: List<PushMessage>) {
        val messages = messages
            .map { it ->
                Message.builder()
                    .setToken(it.deviceToken)
                    .putData("title", it.title)
                    .putData("body", it.content)
                    .putData("image", it.imageUrl)
                    .putData("topicId", it.topicId.toString())
                    .build()
            }

        FirebaseMessaging.getInstance().sendEach(messages)
    }

    private fun createFrom(topic: Topic, subscriber: Visitor): PushMessage {
        return PushMessage(
            deviceToken = subscriber.token!!,
            title = "새로운 후속기사가 도착했어요",
            content = "${topic.title}의 후속기사를 보려면 클릭",
            imageUrl = topic.imageUrl,
            topicId = topic.id!!,
            userId = TODO()
        )
    }

    private fun appendPushLog(messages: List<PushMessage>) {
        val logs = messages.map { it -> PushLog(it) }
        pushLogRepository.saveAll(logs)
    }
}