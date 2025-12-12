package com.flownews.api.event.app

data class EventLikeResponse(
    val isActive: Boolean,
    val message: String,
    val count: Long,
) {
    companion object {
        fun ok() {
            EventLikeResponse(
                isActive = true,
                message = "좋아요가 추가되었습니다.",
                count = 0L,
            )
        }
    }
}
