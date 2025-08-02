package com.flownews.api.common.api

data class ApiResponse<T>(
    val code: String,
    val message: String?,
    val data: T? = null,
) {
    companion object {
        fun <T> ok(data: T): ApiResponse<T> = ApiResponse("200", "성공", data)

        fun ok(): ApiResponse<Void?> = ApiResponse("200", "성공", null)

        fun badRequest(message: String?): ApiResponse<Void?> = ApiResponse("400", message, null)

        fun badRequest(): ApiResponse<Void?> = ApiResponse("400", null, null)

        fun unauthorized(): ApiResponse<Void?> = ApiResponse("401", "unauthorized", null)

        fun error(message: String): ApiResponse<Void?> = ApiResponse("500", message, null)
    }
}
