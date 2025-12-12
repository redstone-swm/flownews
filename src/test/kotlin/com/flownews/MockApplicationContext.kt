package com.flownews

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class MockApplicationContext {
    @Bean
    @Primary
    fun mockRecommendationApiUrl(): String = "http://localhost:8081"
}
