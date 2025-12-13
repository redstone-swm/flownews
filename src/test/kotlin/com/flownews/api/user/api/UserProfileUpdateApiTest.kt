package com.flownews.api.user.api

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
class UserProfileUpdateApiTest {
    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        this.mockMvc =
            MockMvcBuilders.standaloneSetup(MockUserProfileUpdateController())
                .apply<StandaloneMockMvcBuilder>(
                    documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()),
                )
                .build()
    }

    @Test
    fun `should document user profile update`() {
        mockMvc.perform(
            post("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .content("""{"nickname": "새로운닉네임", "birthDate": "1990-05-15", "gender": "MALE"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "user-profile-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("nickname").description("User nickname").optional(),
                        fieldWithPath("birthDate").description("User birth date in YYYY-MM-DD format").optional(),
                        fieldWithPath("gender").description("User gender (MALE, FEMALE, OTHER)").optional(),
                    ),
                    responseFields(
                        fieldWithPath("status").description("Response status (SUCCESS, ERROR)"),
                        fieldWithPath("message").description("Response message"),
                        fieldWithPath("data").description("Response data (null for this endpoint)").optional(),
                    ),
                ),
            )
    }

    @Test
    fun `should document device token update`() {
        mockMvc.perform(
            post("/api/users/device-token")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .content("""{"deviceToken": "fcm_device_token_12345", "deviceType": "ANDROID"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "user-device-token-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("deviceToken").description("FCM device token for push notifications"),
                        fieldWithPath("deviceType").description("Device type (ANDROID, IOS)"),
                    ),
                    responseFields(
                        fieldWithPath("status").description("Response status (SUCCESS, ERROR)"),
                        fieldWithPath("message").description("Response message"),
                        fieldWithPath("data").description("Response data (null for this endpoint)").optional(),
                    ),
                ),
            )
    }

    @Test
    fun `should document user withdrawal`() {
        mockMvc.perform(
            post("/api/users/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                .content("""{"reason": "서비스 불만족", "feedback": "앱 속도가 너무 느림"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json"))
            .andDo(
                document(
                    "user-withdrawal",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("reason").description("Reason for withdrawal"),
                        fieldWithPath("feedback").description("Additional feedback").optional(),
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
    class MockUserProfileUpdateController {
        @PostMapping("/api/users/profile")
        fun updateProfile(
            @RequestBody request: Map<String, Any?>,
        ): Map<String, Any?> {
            return mapOf(
                "status" to "SUCCESS",
                "message" to "프로필이 성공적으로 업데이트되었습니다.",
                "data" to null,
            )
        }

        @PostMapping("/api/users/device-token")
        fun updateDeviceToken(
            @RequestBody request: Map<String, Any>,
        ): Map<String, Any?> {
            return mapOf(
                "status" to "SUCCESS",
                "message" to "디바이스 토큰이 성공적으로 업데이트되었습니다.",
                "data" to null,
            )
        }

        @PostMapping("/api/users/withdraw")
        fun withdraw(
            @RequestBody request: Map<String, Any>,
        ): Map<String, Any?> {
            return mapOf(
                "status" to "SUCCESS",
                "message" to "회원 탈퇴가 성공적으로 처리되었습니다.",
                "data" to null,
            )
        }
    }
}
