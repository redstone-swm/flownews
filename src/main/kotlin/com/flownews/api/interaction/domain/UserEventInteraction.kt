package com.flownews.api.interaction.domain

import BaseEntity
import com.flownews.api.event.domain.Event
import com.flownews.api.user.domain.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "user_event_interactions")
class UserEventInteraction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    val event: Event,
    @Column(name = "interaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val interactionType: InteractionType,
    @Column(name = "additional_data")
    val additionalData: String? = null,
) : BaseEntity()
