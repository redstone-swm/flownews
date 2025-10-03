package com.flownews.api.topic.domain

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TopicSubscriptionRepository : CrudRepository<TopicSubscription, TopicSubscriptionId> {
    @EntityGraph(attributePaths = ["user"])
    fun findByTopicId(
        @Param("topicId") topicId: Long,
    ): List<TopicSubscription>

    @EntityGraph(attributePaths = ["topic"])
    fun findByUserId(
        @Param("userId") userId: Long,
    ): List<TopicSubscription>

    fun existsByTopicIdAndUserId(
        @Param("topicId") topicId: Long,
        @Param("userId") userId: Long,
    ): Boolean

    fun findByTopicIdAndUserId(
        @Param("topicId") topicId: Long,
        @Param("userId") userId: Long,
    ): TopicSubscription?
}
