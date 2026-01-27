package com.flownews.api.user.api

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document
import com.flownews.api.user.app.UserUpdateService
import com.flownews.testutils.ApiResponseFieldSpecs
import com.flownews.testutils.MockMvcTestUtils
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
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

@ExtendWith(RestDocumentationExtension::class)
class UserProfileUpdateApiTest {
    private lateinit var mockMvc: MockMvc

    private val userUpdateService = mockk<UserUpdateService>(relaxed = true)

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        val controller = UserProfileUpdateApi(userUpdateService)
        this.mockMvc = MockMvcTestUtils.createMockMvc(controller, restDocumentation)
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "user-profile-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("nickname").description("사용자 닉네임").optional(),
                        fieldWithPath("birthDate").description("사용자 생년월일 (YYYY-MM-DD 형식)").optional(),
                        fieldWithPath("gender").description("사용자 성별 (MALE, FEMALE, OTHER)").optional(),
                    ),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithOptionalData(),
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "user-device-token-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("deviceToken").description("푸시 알림용 FCM 디바이스 토큰"),
                        fieldWithPath("deviceType").description("디바이스 유형 (ANDROID, IOS)"),
                    ),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithOptionalData(),
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "user-withdrawal",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("reason").description("탈퇴 사유"),
                        fieldWithPath("feedback").description("추가 피드백").optional(),
                    ),
                    responseFields(
                        *ApiResponseFieldSpecs.responseWithOptionalData(),
                    ),
                ),
            )
    }
}
