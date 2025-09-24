package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class TopicListQueryService(
    private val topicRepository: TopicRepository,
) {
    fun getTopics(): List<TopicSummaryResponse> = topicRepository.findAll().map(TopicSummaryResponse::fromEntity)

    private fun getTopicByIds(ids: List<Long>): List<Topic> {
        val unordered = topicRepository.findAllById(ids).toList()
        val topicMap = unordered.associateBy { it.id }
        return ids.mapNotNull { topicMap[it] }
    }
}
