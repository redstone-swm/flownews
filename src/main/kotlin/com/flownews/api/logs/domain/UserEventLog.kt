package com.flownews.api.logs.domain

import com.flownews.api.logs.domain.enums.UserEventType
import com.google.gson.GsonBuilder
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "user_event_logs")
class UserEventLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    val eventType: UserEventType,
    @Column(name = "event_time")
    val eventTime: LocalDateTime,
    @Column(name = "ip_address")
    val ipAddress: String,
    @Column(name = "param", columnDefinition = "text")
    val param: String,
) {
    constructor(
        eventType: UserEventType,
        ipAddress: String,
        param: Map<String, Any?>,
    ) : this(
        eventType = eventType,
        eventTime = LocalDateTime.now(),
        ipAddress = ipAddress,
        param = GsonBuilder().serializeNulls().create().toJson(param),
    )
}
