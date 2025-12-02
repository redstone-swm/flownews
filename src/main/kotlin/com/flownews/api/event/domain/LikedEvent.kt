package com.flownews.api.event.domain

data class LikedEvent(val event: Event, val isLiked: Boolean = false)
