package com.flownews.api.topic.app

import com.flownews.api.logs.domain.UserEventLog
import com.flownews.api.logs.domain.UserEventLogger
import com.flownews.api.logs.domain.enums.UserEventType
import com.flownews.api.topic.domain.TopicHistory
import com.flownews.api.topic.domain.TopicHistoryRepository
import com.flownews.api.user.app.CustomOAuth2User
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TopicHistoryRecordService(
    private val topicHistoryRepository: TopicHistoryRepository,
    private val userEventLogger: UserEventLogger
) {

    @Transactional
    fun recordHistory(req: TopicHistoryRecordRequest, user: CustomOAuth2User?) {
        if (user != null) {
            recordHistoryInternal(req, user.getUser().id!!)
        }

        logUserEvent(req)
    }

    private fun recordHistoryInternal(req: TopicHistoryRecordRequest, userId: Long) {
        val history = getTopicHistory(req, userId)
        history.updateLastReadEvent(req.eventId)

        topicHistoryRepository.save(history)
    }

    private fun getTopicHistory(req: TopicHistoryRecordRequest, userId: Long): TopicHistory {
        return topicHistoryRepository.findByTopicIdAndUserId(req.topicId, userId)
            ?: TopicHistory(
                topicId = req.topicId,
                eventId = req.eventId,
                userId = userId
            )
    }

    private fun logUserEvent(req: TopicHistoryRecordRequest): UserEventLog {
        val userEventLog = UserEventLog(
            eventType = UserEventType.SWIPE,
            eventTime = LocalDateTime.now(),
            ipAddress = req.ipAddress,
            param = mapOf(
                "topicId" to req.topicId,
                "eventId" to req.eventId,
                "direction" to req.direction,
                "elapsedTime" to req.elapsedTime
            )
        )
        return userEventLogger.save(userEventLog)
    }
}