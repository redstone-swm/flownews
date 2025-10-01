package com.flownews.api.topic.domain

import BaseEntity
import com.flownews.api.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@IdClass(TopicSubscriptionId::class)
@Table(name = "subscriptions")
class TopicSubscription(
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User,
    @Id
    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id")
    var topic: Topic,
    @Column(name = "subscription_date")
    var subscriptionDate: LocalDateTime? = null,
    @Column(name = "push_enabled")
    var pushEnabled: Boolean? = null,
) : BaseEntity()
