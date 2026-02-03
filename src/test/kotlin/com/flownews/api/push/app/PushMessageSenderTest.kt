package com.flownews.api.push.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.event.domain.Event
import com.flownews.api.push.domain.PushLog
import com.flownews.api.push.domain.PushLogRepository
import com.flownews.api.push.domain.PushMessage
import com.flownews.api.push.infra.MessageSender
import com.flownews.api.topic.domain.Topic
import com.flownews.api.topic.domain.TopicEvent
import com.flownews.api.topic.domain.TopicQueryService
import com.flownews.api.topic.domain.TopicWithSubscribers
import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.enums.Role
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class PushMessageSenderTest {
    private lateinit var pushMessageSender: PushMessageSender
    private val topicQueryService = mockk<TopicQueryService>()
    private val pushLogRepository = mockk<PushLogRepository>()
    private val messageSender = mockk<MessageSender>()

    @BeforeEach
    fun setUp() {
        pushMessageSender =
            PushMessageSender(
                topicQueryService = topicQueryService,
                pushLogRepository = pushLogRepository,
                messageSender = messageSender,
            )
    }

    @Test
    @DisplayName("여러 활성 구독자가 있는 토픽에 푸시 알림을 전송한다")
    fun `sendPushMessages_ShouldSendToMultipleActiveSubscribers_WhenTopicHasActiveUsers`() {
        // Given
        val topicId = 1L
        val topic = createTopic(id = 1L, title = "AI 기술")
        val activeUser1 = createUser(id = 1L, deviceToken = "token1")
        val activeUser2 = createUser(id = 2L, deviceToken = "token2")
        val topicWithSubscribers =
            TopicWithSubscribers(
                topic = topic,
                subscribers = listOf(activeUser1, activeUser2),
            )

        val messagesSlot = slot<List<PushMessage>>()
        val logsSlot = slot<List<PushLog>>()

        every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
        justRun { messageSender.sendMessages(capture(messagesSlot)) }
        every { pushLogRepository.saveAll(capture(logsSlot)) } returns listOf()

        // When
        pushMessageSender.sendPushMessages(topicId)

        // Then
        verify(exactly = 1) { topicQueryService.getTopicWithSubscribers(topicId) }
        verify(exactly = 1) { messageSender.sendMessages(any<List<PushMessage>>()) }
        verify(exactly = 1) { pushLogRepository.saveAll(any<List<PushLog>>()) }

        val sentMessages = messagesSlot.captured
        assertThat(sentMessages).hasSize(2)
        assertThat(sentMessages.map { it.userId }).containsExactlyInAnyOrder(1L, 2L)
        assertThat(sentMessages.map { it.deviceToken }).containsExactlyInAnyOrder("token1", "token2")
        assertThat(sentMessages.all { it.topicId == 1L }).isTrue()
        assertThat(sentMessages.all { it.title == "새로운 후속기사가 도착했어요" }).isTrue()
        assertThat(sentMessages.all { it.content == "AI 기술의 후속기사를 보려면 클릭" }).isTrue()

        val savedLogs = logsSlot.captured
        assertThat(savedLogs).hasSize(2)
        assertThat(savedLogs.map { it.userId }).containsExactlyInAnyOrder(1L, 2L)
        assertThat(savedLogs.map { it.token }).containsExactlyInAnyOrder("token1", "token2")
    }

    @Test
    @DisplayName("활성 구독자가 없는 토픽에서 빈 목록으로 처리한다")
    fun `sendPushMessages_ShouldHandleEmptyList_WhenTopicHasNoActiveSubscribers`() {
        // Given
        val topicId = 1L
        val topic = createTopic(id = 1L, title = "머신러닝")
        val topicWithSubscribers =
            TopicWithSubscribers(
                topic = topic,
                subscribers = emptyList(),
            )

        val messagesSlot = slot<List<PushMessage>>()
        val logsSlot = slot<List<PushLog>>()

        every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
        justRun { messageSender.sendMessages(capture(messagesSlot)) }
        every { pushLogRepository.saveAll(capture(logsSlot)) } returns listOf()

        // When
        pushMessageSender.sendPushMessages(topicId)

        // Then
        verify(exactly = 1) { topicQueryService.getTopicWithSubscribers(topicId) }
        verify(exactly = 1) { messageSender.sendMessages(any<List<PushMessage>>()) }
        verify(exactly = 1) { pushLogRepository.saveAll(any<List<PushLog>>()) }

        assertThat(messagesSlot.captured).isEmpty()
        assertThat(logsSlot.captured).isEmpty()
    }

    @Test
    @DisplayName("디바이스 토큰이 없는 구독자를 제외하고 푸시 알림을 전송한다")
    fun `sendPushMessages_ShouldFilterInactiveUsers_WhenSubscribersHaveNoDeviceToken`() {
        // Given
        val topicId = 1L
        val topic = createTopic(id = 1L, title = "데이터 사이언스")
        val activeUser = createUser(id = 1L, deviceToken = "token1")
        val inactiveUser1 = createUser(id = 2L, deviceToken = null)
        val inactiveUser2 = createUser(id = 3L, deviceToken = null)
        val topicWithSubscribers =
            TopicWithSubscribers(
                topic = topic,
                subscribers = listOf(activeUser, inactiveUser1, inactiveUser2),
            )

        val messagesSlot = slot<List<PushMessage>>()

        every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
        justRun { messageSender.sendMessages(capture(messagesSlot)) }
        every { pushLogRepository.saveAll<PushLog>(any()) } returns listOf()

        // When
        pushMessageSender.sendPushMessages(topicId)

        // Then
        val sentMessages = messagesSlot.captured
        assertThat(sentMessages).hasSize(1)
        assertThat(sentMessages[0].userId).isEqualTo(1L)
        assertThat(sentMessages[0].deviceToken).isEqualTo("token1")
    }

    @Test
    @DisplayName("토픽 조회 실패시 예외를 전파한다")
    fun `sendPushMessages_ShouldPropagateException_WhenTopicQueryServiceFails`() {
        // Given
        val topicId = 1L
        val exception = RuntimeException("Topic not found")

        every { topicQueryService.getTopicWithSubscribers(topicId) } throws exception

        // When & Then
        assertThatThrownBy { pushMessageSender.sendPushMessages(topicId) }
            .isInstanceOf(NoDataException::class.java)
            .hasMessage("Topic not found")

        verify(exactly = 1) { topicQueryService.getTopicWithSubscribers(topicId) }
        verify(exactly = 0) { messageSender.sendMessages(any<List<PushMessage>>()) }
        verify(exactly = 0) { pushLogRepository.saveAll(any<List<PushLog>>()) }
    }

    // 테스트 데이터 생성 헬퍼 메서드들
    private fun createTopic(
        id: Long,
        title: String,
    ): Topic {
        val topic =
            Topic(
                id = id,
                title = title,
                description = "테스트 설명",
            )

        // 마지막 이벤트 설정을 위한 TopicEvent 생성
        val event =
            mockk<Event> {
                every { requireId() } returns 100L
                every { eventTime } returns LocalDateTime.now()
            }

        val topicEvent =
            mockk<TopicEvent> {
                every { this@mockk.event } returns event
            }

        topic.topicEvents = mutableListOf(topicEvent)

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
