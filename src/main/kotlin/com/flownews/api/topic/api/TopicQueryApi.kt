package com.flownews.api.topic.api

import com.flownews.api.common.app.NoDataException
import com.flownews.api.topic.app.TopicQueryResponse
import com.flownews.api.topic.app.TopicQueryService
import com.flownews.api.user.infra.CustomOAuth2User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Topics", description = "토픽 조회 API")
class TopicQueryApi(
    private val topicQueryService: TopicQueryService,
) {
    @Operation(
        summary = "특정 토픽 상세 조회",
        description = "토픽 ID로 특정 토픽의 상세 정보와 관련 이벤트들을 조회합니다. 사용자의 팔로우 상태도 함께 반환됩니다.",
    )
    @GetMapping("/topics/{topicId}")
    fun getTopic(
        @PathVariable topicId: Long,
        @AuthenticationPrincipal principal: CustomOAuth2User,
    ): ResponseEntity<TopicQueryResponse> =
        try {
            ResponseEntity.ok(topicQueryService.getTopic(principal.getUser(), topicId))
        } catch (e: NoDataException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
}
