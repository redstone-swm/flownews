package com.flownews.api.reaction.domain

enum class ReactionType(
    val id: Long,
    val displayName: String,
) {
    LIKE(1, "좋아요"),
}
