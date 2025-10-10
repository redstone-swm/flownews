package com.flownews.api.notification.dto

import com.flownews.api.push.domain.PushLog
import java.time.LocalDateTime

data class NotificationResponse(
    val title: String,
    val body: String,
    val sentAt: LocalDateTime
) {
    companion object {
        fun from(pushLog: PushLog): NotificationResponse {
            return NotificationResponse(
                title = pushLog.messageTitle,
                body = pushLog.messageBody,
                sentAt = pushLog.sentAt
            )
        }
    }
}