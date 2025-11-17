package com.flownews.api.topic.domain

data class FollowedTopic(val topic: Topic, val isFollowed: Boolean = false)
