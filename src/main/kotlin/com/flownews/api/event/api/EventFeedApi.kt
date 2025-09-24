package com.flownews.api.event.api

import com.flownews.api.event.app.EventFeedService
import com.flownews.api.event.app.EventSummaryResponse
import com.flownews.api.user.infra.CustomOAuth2User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Event Feed", description = "사용자별 이벤트 피드 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/events")
class EventFeedApi(
    private val eventFeedService: EventFeedService
) {
    @Operation(
        summary = "사용자별 이벤트 피드 조회",
        description = "인증된 사용자의 개인화된 이벤트 피드를 반환합니다. 현재는 모든 이벤트를 반환하며, 향후 추천 알고리즘이 적용될 예정입니다.",
    )
    @GetMapping("/feed")
    fun getUserEventFeed(
        @AuthenticationPrincipal principal: CustomOAuth2User
    ): ResponseEntity<List<EventSummaryResponse>> {
        val events = eventFeedService.getUserEventFeed(principal.getUser())
        return ResponseEntity.ok(events)
    }
}