package com.flownews.api.event.api

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.flownews.api.event.app.EventFeedQueryResponse
import com.flownews.api.event.app.EventFeedQueryService
import com.flownews.api.event.app.TopicSimpleInfo
import com.flownews.testutils.ApiResponseFieldSpecs
import com.flownews.testutils.MockCurrentUserArgumentResolver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.time.LocalDateTime

@ExtendWith(RestDocumentationExtension::class, MockitoExtension::class)
class EventFeedQueryApiTest {
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var eventFeedQueryService: EventFeedQueryService

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val controller = EventFeedQueryApi(eventFeedQueryService)

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
    fun `should document event feed query`() {
        val mockResponse =
            listOf(
                EventFeedQueryResponse(
                    id = 1L,
                    topic = TopicSimpleInfo(1L, "AI 기술"),
                    title = "AI 기술의 새로운 혁신",
                    description = "최신 AI 기술 동향과 발전 방향",
                    imageUrl = "https://example.com/image1.jpg",
                    eventTime = LocalDateTime.of(2024, 12, 4, 10, 30, 0),
                    likeCount = 15L,
                ),
            )

        `when`(eventFeedQueryService.getEventFeeds(any(), any())).thenReturn(mockResponse)

        mockMvc.perform(
            get("/api/events/feed")
                .param("category", "technology"),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "event-feed-query",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("category")
                            .description("이벤트 카테고리 필터 (선택사항)")
                            .optional(),
                    ),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithData("이벤트 피드 데이터 배열"),
                        fieldWithPath("data[].id").description("이벤트 ID"),
                        fieldWithPath("data[].topic").description("토픽 정보"),
                        fieldWithPath("data[].topic.id").description("토픽 ID"),
                        fieldWithPath("data[].topic.title").description("토픽 제목"),
                        fieldWithPath("data[].title").description("이벤트 제목"),
                        fieldWithPath("data[].description").description("이벤트 설명"),
                        fieldWithPath("data[].imageUrl").description("이벤트 이미지 URL"),
                        fieldWithPath("data[].eventTime").description("이벤트 발생 시간 (ISO 8601 형식)"),
                        fieldWithPath("data[].likeCount").description("이벤트 좋아요 수"),
                    ),
                ),
            )
    }
}
