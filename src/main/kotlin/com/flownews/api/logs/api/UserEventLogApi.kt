package com.flownews.api.logs.api

import com.flownews.api.logs.domain.UserEventLog
import com.flownews.api.logs.domain.UserEventLogger
import com.flownews.api.logs.domain.enums.UserEventType
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserEventLogApi(
    private val userEventLogger: UserEventLogger,
) {
    @PostMapping("/logs/{eventType}")
    fun appendEventLog(
        @PathVariable eventType: String,
        @RequestBody req: Map<String, Any>,
        request: HttpServletRequest,
    ): ResponseEntity<Void> {
        val type = UserEventType.fromCode(eventType)

        val log =
            UserEventLog(
                eventType = type,
                ipAddress = request.remoteAddr,
                param = extractLog(type, req),
            )

        userEventLogger.save(log)

        return ResponseEntity.ok().build()
    }

    private fun extractLog(
        eventType: UserEventType,
        req: Map<String, Any?>,
    ): Map<String, Any?> =
        when (eventType) {
            UserEventType.FEEDBACK ->
                mapOf(
                    "topicId" to req["topicId"],
                    "score" to req["score"],
                )
            UserEventType.TOPIC_SUGGESTION ->
                mapOf(
                    "content" to req["content"],
                )
            UserEventType.ROUTE ->
                mapOf(
                    "userId" to req["userId"],
                    "action" to req["action"],
                    "userAgent" to req["userAgent"],
                    "referer" to req["referer"],
                    "page" to req["page"],
                )
            else -> mapOf("message" to "Unsupported event type: $eventType")
        }
}
