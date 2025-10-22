package com.flownews.api.push.infra

import com.flownews.api.push.domain.PushMessage
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service

@Service
class FirebaseCloudMessageSender(
    private val firebaseMessaging: FirebaseMessaging,
) : MessageSender {
    override fun sendMessages(messages: List<PushMessage>) {
        if (messages.isEmpty()) return

        val firebaseMessages =
            messages
                .map { it ->
                    Message
                        .builder()
                        .setToken(it.deviceToken)
                        .setNotification(
                            Notification
                                .builder()
                                .setTitle(it.title)
                                .setBody(it.content)
                                .build(),
                        ).putData("topicId", it.topicId.toString())
                        .putData("eventId", it.eventId.toString())
                        .build()
                }

        firebaseMessaging.sendEach(firebaseMessages)
    }
}
