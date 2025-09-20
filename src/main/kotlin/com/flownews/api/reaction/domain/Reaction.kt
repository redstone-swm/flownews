package com.flownews.api.reaction.domain

import BaseEntity
import com.flownews.api.event.domain.Event
import com.flownews.api.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "reactions")
class Reaction(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    val event: Event,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction_type_id")
    val reactionType: ReactionType,
    @Column(name = "is_deleted")
    var isDeleted: LocalDateTime? = null
) : BaseEntity()