package com.flownews.api.notification.api

import com.flownews.api.notification.app.NotificationService
import com.flownews.api.notification.dto.NotificationResponse
import com.flownews.api.user.infra.CustomOAuth2User
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SecurityRequirement(name = "bearerAuth")
@RestController
class NotificationApi(
    private val notificationService: NotificationService
) {
    @GetMapping("/notifications")
    fun getNotifications(
        @AuthenticationPrincipal principal: CustomOAuth2User
    ): ResponseEntity<List<NotificationResponse>> {
        val notifications = notificationService.getRecentNotifications(principal.getUser().id!!)
        return ResponseEntity.ok(notifications)
    }
}