package com.flownews.api.topic.domain

import com.flownews.api.user.domain.User

data class TopicWithSubscribers(
    val topic: Topic,
    val subscribers: List<User>,
) {
    fun getActiveSubscribers(): List<User> = subscribers.filter { it.deviceToken != null }
}
