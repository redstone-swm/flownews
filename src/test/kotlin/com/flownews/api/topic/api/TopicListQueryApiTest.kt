package com.flownews.api.topic.api

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.flownews.api.topic.app.TopicListQueryResponse
import com.flownews.api.topic.app.TopicListQueryService
import com.flownews.api.topic.app.TopicTopKQueryResponse
import com.flownews.testutils.ApiResponseFieldSpecs
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

@ExtendWith(RestDocumentationExtension::class, MockitoExtension::class)
class TopicListQueryApiTest {
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var topicListQueryService: TopicListQueryService

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val controller = TopicListQueryApi(topicListQueryService)

        this.mockMvc =
            MockMvcBuilders.standaloneSetup(controller)
                .apply<StandaloneMockMvcBuilder>(
                    documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()),
                )
                .build()
    }

    @Test
    fun `should document all topics query`() {
        val mockTopics =
            listOf(
                TopicListQueryResponse(
                    id = 1L,
                    title = "인공지능 발전",
                    description = "AI 기술의 최신 동향과 발전사항",
                ),
                TopicListQueryResponse(
                    id = 2L,
                    title = "블록체인 기술",
                    description = "블록체인과 암호화폐의 최신 소식",
                ),
            )

        `when`(topicListQueryService.getTopics(any())).thenReturn(mockTopics)

        mockMvc.perform(
            get("/api/topics")
                .param("page", "0")
                .param("size", "10"),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "topic-list-query",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("page").description("Page number (0-based, optional)").optional(),
                        parameterWithName("size").description("Page size (optional)").optional(),
                    ),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithData("Topic list data as array"),
                        fieldWithPath("data[].id").description("Topic ID"),
                        fieldWithPath("data[].title").description("Topic title"),
                        fieldWithPath("data[].description").description("Topic description"),
                    ),
                ),
            )
    }

    @Test
    fun `should document top k topics query`() {
        val mockTopKTopics =
            listOf(
                TopicTopKQueryResponse(
                    id = 1L,
                    title = "인공지능 발전",
                ),
            )

        `when`(topicListQueryService.getTopKTopics(any())).thenReturn(mockTopKTopics)

        mockMvc.perform(
            get("/api/topics/topk")
                .param("limit", "5"),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "topic-topk-query",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName(
                            "limit",
                        ).description("Number of top topics to retrieve (default: 5)").optional(),
                    ),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithData("Top K topics data as array"),
                        fieldWithPath("data[].id").description("Topic ID"),
                        fieldWithPath("data[].title").description("Topic title"),
                    ),
                ),
            )
    }

    @Test
    fun `should document topics search query`() {
        val mockSearchResults =
            listOf(
                TopicListQueryResponse(
                    id = 1L,
                    title = "인공지능 발전",
                    description = "AI 기술의 최신 동향과 발전사항",
                ),
            )

        `when`(topicListQueryService.getTopicsByKeyword(any())).thenReturn(mockSearchResults)

        mockMvc.perform(
            get("/api/topics/search")
                .param("keyword", "AI"),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "topic-search-query",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    queryParameters(
                        parameterWithName("keyword").description("Search keyword for topic title or description"),
                    ),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithData("Search results data as array"),
                        fieldWithPath("data[].id").description("Topic ID"),
                        fieldWithPath("data[].title").description("Topic title"),
                        fieldWithPath("data[].description").description("Topic description"),
                    ),
                ),
            )
    }
}
