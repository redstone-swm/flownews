package com.flownews.api.push.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.event.domain.Event
import com.flownews.api.push.domain.PushLog
import com.flownews.api.push.domain.PushLogRepository
import com.flownews.api.push.domain.PushMessage
import com.flownews.api.push.infra.MessageSender
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicEvent
import com.flownews.api.topic.domain.TopicEventId
import com.flownews.api.topic.domain.TopicQueryService
import com.flownews.api.topic.domain.TopicWithSubscribers
import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.enums.Role
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.time.LocalDateTime

class PushMessageSenderTest : FunSpec({
    val topicQueryService = mockk<TopicQueryService>()
    val pushLogRepository = mockk<PushLogRepository>()
    val messageSender = mockk<MessageSender>()
    lateinit var pushMessageSender: PushMessageSender

    beforeTest {
        clearMocks(topicQueryService, pushLogRepository, messageSender)
        pushMessageSender =
            PushMessageSender(
                topicQueryService = topicQueryService,
                pushLogRepository = pushLogRepository,
                messageSender = messageSender,
            )
    }

    test("sendPushMessages should handle empty list when topic has no active subscribers") {
        val topicId = 1L
        val topicWithSubscribers = createTopicWithNoSubscribers(topicId)

        every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers

        pushMessageSender.sendPushMessages(topicId)

        verify(exactly = 0) { messageSender.sendMessages(any<List<PushMessage>>()) }
        verify(exactly = 0) { pushLogRepository.saveAll(any<List<PushLog>>()) }
    }

    test("sendPushMessages should filter inactive users when subscribers have no device token") {
        val topicId = 1L
        val topicWithSubscribers = createTopicWithMixedSubscribers(topicId)
        val messagesSlot = slot<List<PushMessage>>()

        every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
        justRun { messageSender.sendMessages(capture(messagesSlot)) }
        every { pushLogRepository.saveAll(any<List<PushLog>>()) } returns listOf()

        pushMessageSender.sendPushMessages(topicId)

        verify(exactly = 1) { messageSender.sendMessages(any<List<PushMessage>>()) }
        verify(exactly = 1) { pushLogRepository.saveAll(any<List<PushLog>>()) }

        val sentMessages = messagesSlot.captured
        sentMessages.size shouldBe 1
    }

    test("sendPushMessages should propagate exception when topic query service fails") {
        val topicId = 1L
        every { topicQueryService.getTopicWithSubscribers(topicId) } throws NoDataException()

        shouldThrow<NoDataException> {
            pushMessageSender.sendPushMessages(topicId)
        }

        verify(exactly = 0) { messageSender.sendMessages(any<List<PushMessage>>()) }
        verify(exactly = 0) { pushLogRepository.saveAll(any<List<PushLog>>()) }
    }
}) {
    companion object {
        private fun createTopicWithNoSubscribers(topicId: Long): TopicWithSubscribers {
            val topic = createTopic(topicId)
            return TopicWithSubscribers(
                topic = topic,
                subscribers = emptyList(),
            )
        }

        private fun createTopicWithMixedSubscribers(topicId: Long): TopicWithSubscribers {
            val topic = createTopic(topicId)
            val activeUser = createUser(id = 1L, deviceToken = "token1")
            val inactiveUser = createUser(id = 2L, deviceToken = null)
            return TopicWithSubscribers(
                topic = topic,
                subscribers = listOf(activeUser, inactiveUser),
            )
        }

        private fun createTopic(id: Long): Topic {
            val event =
                Event(
                    id = 100L,
                    eventTime = LocalDateTime.now(),
                    title = "title",
                    description = "description",
                    imageUrl = "url",
                    category = "category",
                )
            val topic =
                Topic(
                    id = id,
                    title = "title",
                    description = "test",
                )
            topic.topicEvents =
                mutableListOf(
                    TopicEvent(
                        TopicEventId(topic.requireId(), event.requireId()),
                        topic,
                        event,
                        LocalDateTime.now(),
                    ),
                )
            return topic
        }

        private fun createUser(
            id: Long,
            deviceToken: String?,
        ): User {
            return User(
                id = id,
                oauthId = "oauth_$id",
                provider = "google",
                name = "Test User $id",
                email = "user$id@test.com",
                profileUrl = null,
                role = Role.USER,
                deviceToken = deviceToken,
            )
        }
    }
}
