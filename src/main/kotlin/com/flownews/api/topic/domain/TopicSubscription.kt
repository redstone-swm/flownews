package com.flownews.api.topic.domain

import BaseEntity
import com.flownews.api.user.domain.User
import jakarta.persistence.*

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
    var topic: Topic
) : BaseEntity()