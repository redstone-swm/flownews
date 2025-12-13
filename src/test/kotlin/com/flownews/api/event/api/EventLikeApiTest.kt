package com.flownews.api.event.api

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.flownews.api.event.app.EventLikeService
import com.flownews.testutils.ApiResponseFieldSpecs
import com.flownews.testutils.MockCurrentUserArgumentResolver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class, MockitoExtension::class)
class EventLikeApiTest {
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var eventLikeService: EventLikeService

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val controller = EventLikeApi(eventLikeService)

        this.mockMvc =
            MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(MockCurrentUserArgumentResolver())
                .apply<StandaloneMockMvcBuilder>(
                    documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()),
                )
                .build()
    }

    @Test
    fun `should document event like toggle`() {
        mockMvc.perform(
            post("/api/events/{eventId}/like", 1L)
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "event-like-toggle",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("eventId").description("이벤트 ID"),
                    ),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithOptionalData("응답 데이터 (이 엔드포인트에서는 null)"),
                    ),
                ),
            )
    }
}
