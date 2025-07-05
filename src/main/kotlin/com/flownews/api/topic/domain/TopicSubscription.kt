package com.flownews.api.topic.domain

import BaseEntity
import com.flownews.api.user.domain.Visitor
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@IdClass(TopicSubscriptionId::class)
@Table(name = "subscriptions")
class TopicSubscription(
    @Id
    @ManyToOne
    @JoinColumn(name = "visitor_id", referencedColumnName = "id")
    var visitor: Visitor,

    @Id
    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id")
    var topic: Topic
) : BaseEntity()