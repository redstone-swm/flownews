package com.flownews.api.user.api

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.flownews.testutils.ApiResponseFieldSpecs
import com.flownews.testutils.MockCurrentUserArgumentResolver
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class, MockitoExtension::class)
class UserQueryApiTest {
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val controller = UserQueryApi()

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
    fun `should document current user query`() {
        mockMvc.perform(
            get("/api/users/me")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "user-current-query",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithData("Current user data"),
                        fieldWithPath("data.id").description("User ID"),
                        fieldWithPath("data.name").description("User name"),
                        fieldWithPath("data.email").description("User email"),
                        fieldWithPath("data.profileUrl").description("User profile image URL").optional(),
                        fieldWithPath("data.role").description("User role (USER, ADMIN)"),
                        fieldWithPath("data.isProfileComplete").description("Whether user profile is complete"),
                    ),
                ),
            )
    }
}
