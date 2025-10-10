package com.flownews.api.notification.app

import com.flownews.api.notification.dto.NotificationResponse
import com.flownews.api.push.domain.PushLogRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val pushLogRepository: PushLogRepository
) {
    fun getRecentNotifications(userId: Long): List<NotificationResponse> {
        val pageable = PageRequest.of(0, 5)
        val pushLogs = pushLogRepository.findByUserIdOrderBySentAtDesc(userId, pageable)
        return pushLogs.map { NotificationResponse.from(it) }
    }
}