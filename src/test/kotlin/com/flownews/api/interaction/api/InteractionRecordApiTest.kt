package com.flownews.api.interaction.api

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@ExtendWith(RestDocumentationExtension::class)
class InteractionRecordApiTest {
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(MockInteractionRecordController())
                .apply<StandaloneMockMvcBuilder>(
                    documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()),
                )
                .build()
    }

    @Test
    fun `should document interaction record`() {
        mockMvc.perform(
            post("/api/interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .content(
                    """
                    {
                        "eventId": 1, 
                        "interactionType": "VIEW", 
                        "metadata": {
                            "source": "mobile_app", 
                            "duration": 30
                        }
                    }
                    """.trimIndent(),
                ),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "interaction-record",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("eventId").description("Event ID that user interacted with"),
                        fieldWithPath("interactionType").description("Type of interaction (VIEW, CLICK, SHARE, etc.)"),
                        fieldWithPath("metadata").description("Additional interaction metadata").optional(),
                        fieldWithPath(
                            "metadata.source",
                        ).description("Source of interaction (mobile_app, web, etc.)").optional(),
                        fieldWithPath("metadata.duration").description("Duration of interaction in seconds").optional(),
                    ),
                    responseFields(
                        fieldWithPath("status").description("Response status (SUCCESS, ERROR)"),
                        fieldWithPath("message").description("Response message"),
                        fieldWithPath("data").description("Response data (null for this endpoint)").optional(),
                    ),
                ),
            )
    }

    @RestController
    class MockInteractionRecordController {
        @PostMapping("/api/interactions")
        fun recordInteraction(
            @RequestBody request: Map<String, Any>,
        ): Map<String, Any?> {
            return mapOf(
                "status" to "SUCCESS",
                "message" to "사용자 상호작용이 성공적으로 기록되었습니다.",
                "data" to null,
            )
        }
    }
}
