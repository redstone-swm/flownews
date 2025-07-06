package com.flownews.api.topic.domain

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TopicSubscriptionRepository : CrudRepository<TopicSubscription, TopicSubscriptionId> {
    @EntityGraph(attributePaths = ["visitor"])
    fun findByTopicId(@Param("topicId") topicId: Long): List<TopicSubscription>

}