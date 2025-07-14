package com.flownews.api.logs.api

import com.flownews.api.logs.domain.UserEventLog
import com.flownews.api.logs.domain.UserEventLogger
import com.flownews.api.logs.domain.enums.UserEventType
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class FeedbackLogRequest(
    val ipAddress: String,
    val topicId: Long,
    val time: LocalDateTime,
    val content: String?,
    val score: Int?
)

@RestController
@RequestMapping
class UserEventLogApi(private val userEventLogger: UserEventLogger) {

    @PostMapping("/logs/feedback")
    fun appendFeedback(@RequestBody req: FeedbackLogRequest): ResponseEntity<Void> {
        val log = UserEventLog(
            eventType = UserEventType.FEEDBACK,
            eventTime = req.time,
            ipAddress = req.ipAddress,
            param = mapOf(
                "topicId" to req.topicId,
                "score" to req.score,
                "content" to req.content
            )
        )

        userEventLogger.save(log)

        return ResponseEntity.ok().build()
    }
}