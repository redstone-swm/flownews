package com.flownews.api.reaction.domain

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
    @Column(name = "reaction_type_id")
    val id: Long? = null,
    @Column(name = "reaction_type_name")
    val name: String
) {
    fun requireId(): Long = id ?: throw IllegalStateException("ReactionType ID cannot be null")
}