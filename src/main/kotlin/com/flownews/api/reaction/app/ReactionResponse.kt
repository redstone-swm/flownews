package com.flownews.api.reaction.app

data class ReactionSummaryResponse(
    val reactionTypeName: String,
    val count: Long
)

data class EventWithReactionsResponse(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val eventTime: String,
    val viewCount: Long,
    val reactions: List<ReactionSummaryResponse>
)