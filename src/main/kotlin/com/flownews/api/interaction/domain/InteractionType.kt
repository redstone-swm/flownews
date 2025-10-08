package com.flownews.api.interaction.domain

enum class InteractionType {
    VIEWED, // 피드에서 이벤트를 보았음
    ARTICLE_CLICKED, // 기사 읽기를 클릭
    TOPIC_VIEWED, // 토픽 상세를 확인
    TOPIC_FOLLOWED, // 토픽 팔로우를 클릭
    TOPIC_UNFOLLOWED, // 토픽 언팔로우를 클릭
}
