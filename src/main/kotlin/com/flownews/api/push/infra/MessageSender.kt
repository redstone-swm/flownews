package com.flownews.api.push.infra

import com.flownews.api.push.domain.PushMessage

interface MessageSender {
    fun sendMessages(messages: List<PushMessage>)
}
