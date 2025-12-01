package com.flownews.api.push.app

import com.flownews.api.push.domain.PushLog
import com.flownews.api.push.domain.PushLogRepository
import com.flownews.api.push.domain.PushMessage
import com.flownews.api.push.infra.MessageSender
import com.flownews.api.topic.domain.TopicQueryService
import org.springframework.stereotype.Service

@Service
class PushMessageSender(
    private val topicQueryService: TopicQueryService,
    private val pushLogRepository: PushLogRepository,
    private val messageSender: MessageSender,
) {
    fun sendPushMessages(topicId: Long) {
        val topicWithSubscribers = topicQueryService.getTopicWithSubscribers(topicId)
        val topic = topicWithSubscribers.topic
        val subscribers = topicWithSubscribers.getActiveSubscribers()
        val messages = subscribers.map { PushMessage(topic, it) }

        messageSender.sendMessages(messages)
        appendPushLog(messages)
    }

    private fun appendPushLog(messages: List<PushMessage>) {
        val logs = messages.map(::PushLog)
        pushLogRepository.saveAll(logs)
    }
}
