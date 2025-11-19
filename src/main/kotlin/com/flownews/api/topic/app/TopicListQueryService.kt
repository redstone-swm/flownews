package com.flownews.api.topic.app

import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TopicListQueryService(
    private val topicRepository: TopicRepository,
) {
    fun getTopics(req: TopicListQueryRequest): List<TopicListQueryResponse> {
        val pageRequest = req.toPageable()
        return topicRepository.findAll(pageRequest)
            .map { TopicListQueryResponse.from(it) }
    }

    fun getTopicsByKeyword(req: TopicListQueryRequest): List<TopicListQueryResponse> {
        val pageable = req.toPageable()
        val keyword = req.getKeyword()
        val topics =
            topicRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword,
                keyword,
                pageable,
            )
        return topics.map { TopicListQueryResponse.from(it) }
    }

    fun getTopKTopics(limit: Int): List<TopicTopKQueryResponse> =
        findTopTopicsSinceLast24Hours(limit).map { TopicTopKQueryResponse(it.requireId(), it.title) }

    fun findTopTopicsSinceLast24Hours(
        limit: Int,
        category: String? = null,
    ): List<Topic> {
        val twentyFourHoursAgo = LocalDateTime.now().minusHours(24).toLocalDate()
        return topicRepository.findTopKTopicsByInteractionsSince(twentyFourHoursAgo, category, limit)
    }
}
