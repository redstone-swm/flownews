package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import com.flownews.api.topic.domain.TopicSectionRepository
import org.springframework.stereotype.Service
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Service
class TopicListQueryService(private val topicRepository: TopicRepository, private val topicSectionRepository: TopicSectionRepository) {

    fun getTopics(): List<TopicSummaryResponse> = topicRepository.findAll().map(TopicSummaryResponse::fromEntity)

    fun getTopicSections(): TopicSectionListQueryResponse {
        val sectionConfigStr = topicSectionRepository.findById(1).map { it.config }.orElseThrow()
        val sections = Json.decodeFromString<MenuConfig>(sectionConfigStr).sections

        return TopicSectionListQueryResponse(
            sections.map {
                val topics = getTopicByIds(it.ids).map(TopicSummaryResponse::fromEntity)
                TopicSectionListItem(it.title, topics)
            })
    }

    private fun getTopicByIds(ids: List<Long>): List<Topic> = topicRepository.findAllById(ids).toList()

}


data class TopicSectionListQueryResponse(
    val sections: List<TopicSectionListItem>
)

data class TopicSectionListItem(
    val title: String,
    val topics: List<TopicSummaryResponse>
)

@Serializable
data class SectionConfig(
    val title: String,
    val ids: List<Long>
)

@Serializable
data class MenuConfig(
    val sections: List<SectionConfig>
)

