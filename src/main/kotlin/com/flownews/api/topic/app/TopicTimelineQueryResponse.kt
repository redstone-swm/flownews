package com.flownews.api.topic.app

import com.flownews.api.event.domain.LikedEvent
import com.flownews.api.event.domain.article.Article
import com.flownews.api.topic.domain.FollowedTopic
import java.time.LocalDateTime

data class TopicTimelineQueryResponse(
    val id: Long,
    val title: String,
    val description: String,
    val events: List<EventItemQueryResponse>,
    val isFollowing: Boolean,
) {
    companion object {
        fun of(
            followedTopic: FollowedTopic,
            events: List<LikedEvent>,
        ) = followedTopic.run {
            TopicTimelineQueryResponse(
                id = topic.requireId(),
                title = topic.title,
                description = topic.description,
                events = events.map { it.toEventItemQueryResponse() },
                isFollowing = isFollowed,
            )
        }
    }
}

data class EventItemQueryResponse(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val eventTime: LocalDateTime,
    val articles: List<ArticleResponse>,
    val likeCount: Long,
    val isActive: Boolean,
)

data class ArticleResponse(
    val id: Long,
    val title: String,
    val source: String,
    val url: String,
) {
    companion object {
        fun fromEntity(article: Article) =
            ArticleResponse(
                id = article.requireId(),
                title = article.title,
                source = article.source,
                url = article.url,
            )
    }
}

fun LikedEvent.toEventItemQueryResponse() =
    EventItemQueryResponse(
        id = event.requireId(),
        title = event.title,
        description = event.description,
        imageUrl = event.imageUrl,
        eventTime = event.eventTime,
        articles = event.articles.map { ArticleResponse.fromEntity(it) },
        likeCount = event.getLikeCount(),
        isActive = isLiked,
    )
