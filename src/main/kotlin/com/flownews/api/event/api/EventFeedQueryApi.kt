package com.flownews.api.event.api

import com.flownews.api.common.api.CurrentUser
import com.flownews.api.event.app.EventFeedQueryResponse
import com.flownews.api.event.app.EventFeedQueryService
import com.flownews.api.user.domain.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Event Feed", description = "사용자별 이벤트 피드 API")
@RestController
class EventFeedQueryApi(
    private val eventFeedQueryService: EventFeedQueryService,
) {
    @Operation(
        summary = "사용자별 이벤트 피드 조회",
        description = "인증된 사용자의 개인화된 이벤트 피드를 상세 정보와 함께 반환합니다.",
    )
    @GetMapping("/api/events/feed")
    fun getUserEventFeed(
        @CurrentUser user: User?,
        @RequestParam(required = false) category: String?,
    ): ResponseEntity<List<EventFeedQueryResponse>> {
        val eventFeed = eventFeedQueryService.getEventFeeds(user, category)
        return ResponseEntity.ok(eventFeed)
    }
}
