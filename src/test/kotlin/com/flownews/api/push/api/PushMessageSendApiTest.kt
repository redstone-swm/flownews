package com.flownews.api.push.api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@ExtendWith(RestDocumentationExtension::class)
class PushMessageSendApiTest {
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(MockPushMessageSendController())
                .apply<StandaloneMockMvcBuilder>(
                    documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()),
                )
                .build()
    }

    @Test
    fun `should document push message send by topic`() {
        mockMvc.perform(
            post("/api/notifications/push?by=topic")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"topicId": 1, "title": "새로운 이벤트", "body": "AI 기술 관련 새로운 이벤트가 등록되었습니다."}"""),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "push-message-send-by-topic",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("by").description("Push message target type (topic)"),
                    ),
                    requestFields(
                        fieldWithPath("topicId").description("Topic ID to send push message to"),
                        fieldWithPath("title").description("Push notification title"),
                        fieldWithPath("body").description("Push notification body content"),
                    ),
                    responseFields(
                        fieldWithPath("status").description("Response status (SUCCESS, ERROR)"),
                        fieldWithPath("message").description("Response message"),
                        fieldWithPath("data").description("Push message send result data"),
                        fieldWithPath("data.sentCount").description("Number of push messages sent"),
                        fieldWithPath("data.targetUsers").description("Number of target users"),
                    ),
                ),
            )
    }

    @RestController
    class MockPushMessageSendController {
        @PostMapping("/api/notifications/push", params = ["by=topic"])
        fun sendPushMessageByTopic(
            @RequestBody request: Map<String, Any>,
            @RequestParam by: String,
        ): Map<String, Any> {
            return mapOf(
                "status" to "SUCCESS",
                "message" to "푸시 메시지가 성공적으로 전송되었습니다.",
                "data" to
                    mapOf(
                        "sentCount" to 15,
                        "targetUsers" to 15,
                    ),
            )
        }
    }
}
