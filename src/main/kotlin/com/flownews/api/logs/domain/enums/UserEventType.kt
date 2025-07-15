package com.flownews.api.logs.domain.enums

enum class UserEventType(val code: String) {
    FEEDBACK("feedback"),
    SWIPE("swipe"),
    TOPIC_SUGGESTION("topic-suggestion");

    companion object {
        fun fromCode(code: String): UserEventType =
            entries.find { it.code == code }
                ?: throw IllegalArgumentException("Unknown event type: $code")
    }
}