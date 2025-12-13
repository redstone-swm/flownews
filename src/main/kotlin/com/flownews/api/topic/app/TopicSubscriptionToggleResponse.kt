package com.flownews.api.topic.app

data class TopicSubscriptionToggleResponse(
    val isSubscribed: Boolean,
    val message: String,
) {
    constructor(isSubscribed: Boolean) : this(
        isSubscribed = isSubscribed,
        message = if (isSubscribed) "토픽을 구독했습니다." else "토픽 구독을 취소했습니다.",
    )
}
