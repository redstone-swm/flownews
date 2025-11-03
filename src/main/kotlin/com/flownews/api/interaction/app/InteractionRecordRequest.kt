package com.flownews.api.interaction.app

import com.flownews.api.interaction.domain.InteractionType

data class InteractionRecordRequest(
    val eventId: Long,
    val interactionType: InteractionType,
    val additionalData: String? = null,
)
