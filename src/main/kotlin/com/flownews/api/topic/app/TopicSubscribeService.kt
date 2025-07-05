//package com.flownews.api.topic.app
//
//import com.flownews.api.common.app.NoDataException
//import com.flownews.api.topic.domain.Topic
//import com.flownews.api.topic.domain.TopicRepository
//import com.flownews.api.topic.domain.TopicSubscription
//import com.flownews.api.topic.domain.TopicSubscriptionId
//import com.flownews.api.topic.domain.TopicSubscriptionRepository
//import com.flownews.api.user.app.VisitorSyncRequest
//import com.flownews.api.user.app.VisitorSyncService
//import jakarta.transaction.Transactional
//import org.springframework.stereotype.Service
//
//@Service
//class TopicSubscribeService(
//    private val topicRepository: TopicRepository,
//    private val topicSubscriptionRepository: TopicSubscriptionRepository,
//    private val visitorSyncService: VisitorSyncService
//) {
//
//    @Transactional
//    fun subscribeTopic(topicId: Long, req: TopicSubscribeRequest): Visitor {
//        val topic = getTopic(topicId)
//        val visitor = visitorSyncService.sync(convert(req))
//        val subscriptionId = TopicSubscriptionId(visitor.id!!, topic.id!!)
//
//        if (topicSubscriptionRepository.existsById(subscriptionId)) {
//            throw IllegalStateException("이미 구독한 토픽입니다")
//        }
//
//        saveSubscription(visitor, topic)
//
//        return visitor
//    }
//
//    private fun convert(req: TopicSubscribeRequest): VisitorSyncRequest {
//        return VisitorSyncRequest(req.visitorId, req.userAgent, req.token)
//    }
//
//    private fun saveSubscription(visitor: Visitor, topic: Topic) {
//        topicSubscriptionRepository.save(TopicSubscription(visitorId = visitor.id!!, topic = topic))
//    }
//
//    private fun getTopic(topicId: Long): Topic {
//        return topicRepository.findById(topicId)
//            .orElseThrow { NoDataException("Topic not found : $topicId") }
//    }
//}