package com.flownews.api.push.app

import com.flownews.api.push.domain.PushLog
import com.flownews.api.push.domain.PushLogRepository
import com.flownews.api.push.domain.PushMessage
import com.flownews.api.topic.app.TopicQueryService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class PushMessageSender(
    private val topicQueryService: TopicQueryService,
    private val pushLogRepository: PushLogRepository,
) {
    fun sendPushMessages(topicId: Long) {
        val topicWithSubscribers = topicQueryService.getTopicWithSubscribers(topicId)
        val topic = topicWithSubscribers.topic
        val subscribers = topicWithSubscribers.getActiveSubscribers()
        val messages = subscribers.map { PushMessage(topic, it) }

        sendPushMessagesInternal(messages)
        appendPushLog(messages)
    }

    // FIXME Firebase 코드 이동 필요
    private fun sendPushMessagesInternal(messages: List<PushMessage>) {
        if (messages.isEmpty()) return

        val firebaseMessages =
            messages
                .map { it ->
                    Message
                        .builder()
                        .setToken(it.deviceToken)
                        .setNotification(
                            Notification
                                .builder()
                                .setTitle(it.title)
                                .setBody(it.content)
                                .build(),
                        ).putData("topicId", it.topicId.toString())
                        .putData("eventId", it.eventId.toString())
                        .build()
                }

        FirebaseMessaging.getInstance().sendEach(firebaseMessages)
    }

    private fun appendPushLog(messages: List<PushMessage>) {
        val logs = messages.map { it -> PushLog(it) }
        pushLogRepository.saveAll(logs)
    }
}
