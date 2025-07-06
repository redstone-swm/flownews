package com.flownews.api.push.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import com.flownews.api.topic.domain.Topic
import com.flownews.api.user.domain.Visitor

@Entity
@Table(name = "push_logs")
data class PushLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "device_token")
    val token: String,

    @Column(name = "message_title")
    val messageTitle: String,

    @Column(name = "message_body")
    val messageBody: String,

    @Column(name = "message_image_url")
    val messageImageUrl: String? = null,

    @Column(name = "sent_at")
    val sentAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(message: PushMessage) : this(
        userId = message.userId,
        token = message.deviceToken,
        messageTitle = message.title,
        messageBody = message.content,
        messageImageUrl = message.imageUrl
    )
}
