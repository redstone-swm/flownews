package com.flownews.testutils

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

object ApiResponseFieldSpecs {
    fun responseWithData(dataDescription: String): Array<FieldDescriptor> =
        arrayOf(
            fieldWithPath("code").description("응답 코드 (200, 400, 500 등)"),
            fieldWithPath("message").description("응답 메시지"),
            fieldWithPath("data").description(dataDescription),
        )

    fun responseWithOptionalData(dataDescription: String): Array<FieldDescriptor> =
        arrayOf(
            fieldWithPath("code").description("응답 코드 (200, 400, 500 등)"),
            fieldWithPath("message").description("응답 메시지"),
            fieldWithPath("data").description(dataDescription).optional(),
        )
}
