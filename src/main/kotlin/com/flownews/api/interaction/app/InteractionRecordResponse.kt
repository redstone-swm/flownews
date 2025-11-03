package com.flownews.api.interaction.app

import com.flownews.api.interaction.domain.UserEventInteraction

data class InteractionRecordResponse(
    val id: Long,
    val success: Boolean,
    val message: String,
) {
    companion object {
        fun ok(interaction: UserEventInteraction) =
            InteractionRecordResponse(
                id = interaction.requireId(),
                success = true,
                message = "상호작용이 성공적으로 기록되었습니다",
            )
    }
}
