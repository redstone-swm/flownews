package com.flownews.api.reaction.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ReactionTypeRepository : JpaRepository<ReactionType, Long> {
    fun findByName(name: String): ReactionType?
}
