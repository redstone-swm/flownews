package com.flownews.api.reaction.domain

import BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "reaction_types")
class ReactionType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "name")
    var name: String,
) : BaseEntity() {
    fun requireId(): Long = id ?: throw IllegalStateException("ReactionType ID cannot be null")
}
