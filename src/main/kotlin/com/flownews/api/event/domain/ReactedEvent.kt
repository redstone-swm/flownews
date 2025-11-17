package com.flownews.api.event.domain

data class ReactedEvent(val event: Event, val isReacted: Boolean = false)
