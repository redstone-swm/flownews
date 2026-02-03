package com.flownews.api.push.app

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
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@DisplayName("PushMessageSender 단위 테스트")
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

    @Nested
    @DisplayName("sendPushMessages 메서드")
    inner class SendPushMessagesTest {
        @Test
        @DisplayName("활성 구독자들에게 푸시 메시지를 성공적으로 전송한다")
        fun `should successfully send push messages to active subscribers`() {
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
            every { pushLogRepository.saveAll<PushLog>(capture(logsSlot)) } returns listOf()

            // When
            pushMessageSender.sendPushMessages(topicId)

            // Then
            verify(exactly = 1) { topicQueryService.getTopicWithSubscribers(topicId) }
            verify(exactly = 1) { messageSender.sendMessages(any<List<PushMessage>>()) }
            verify(exactly = 1) { pushLogRepository.saveAll<PushLog>(any<List<PushLog>>()) }

            // 전송된 메시지 검증
            val sentMessages = messagesSlot.captured
            assertThat(sentMessages).hasSize(2)
            assertThat(sentMessages.map { it.userId }).containsExactlyInAnyOrder(1L, 2L)
            assertThat(sentMessages.map { it.deviceToken }).containsExactlyInAnyOrder("token1", "token2")
            assertThat(sentMessages.all { it.topicId == 1L }).isTrue()
            assertThat(sentMessages.all { it.title == "새로운 후속기사가 도착했어요" }).isTrue()
            assertThat(sentMessages.all { it.content == "AI 기술의 후속기사를 보려면 클릭" }).isTrue()

            // 로그 저장 검증
            val savedLogs = logsSlot.captured
            assertThat(savedLogs).hasSize(2)
            assertThat(savedLogs.map { it.userId }).containsExactlyInAnyOrder(1L, 2L)
            assertThat(savedLogs.map { it.token }).containsExactlyInAnyOrder("token1", "token2")
        }

        @Test
        @DisplayName("단일 활성 구독자에게 푸시 메시지를 성공적으로 전송한다")
        fun `should successfully send push message to single active subscriber`() {
            // Given
            val topicId = 1L
            val topic = createTopic(id = 1L, title = "블록체인")
            val activeUser = createUser(id = 1L, deviceToken = "token1")
            val topicWithSubscribers =
                TopicWithSubscribers(
                    topic = topic,
                    subscribers = listOf(activeUser),
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

            val message = sentMessages[0]
            assertThat(message.userId).isEqualTo(1L)
            assertThat(message.deviceToken).isEqualTo("token1")
            assertThat(message.topicId).isEqualTo(1L)
            assertThat(message.title).isEqualTo("새로운 후속기사가 도착했어요")
            assertThat(message.content).isEqualTo("블록체인의 후속기사를 보려면 클릭")
        }

        @Test
        @DisplayName("활성 구독자가 없으면 빈 목록으로 메시지 전송과 로그를 처리한다")
        fun `should handle empty active subscribers list`() {
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
            every { pushLogRepository.saveAll<PushLog>(capture(logsSlot)) } returns listOf()

            // When
            pushMessageSender.sendPushMessages(topicId)

            // Then
            verify(exactly = 1) { topicQueryService.getTopicWithSubscribers(topicId) }
            verify(exactly = 1) { messageSender.sendMessages(any<List<PushMessage>>()) }
            verify(exactly = 1) { pushLogRepository.saveAll<PushLog>(any<List<PushLog>>()) }

            assertThat(messagesSlot.captured).isEmpty()
            assertThat(logsSlot.captured).isEmpty()
        }

        @Test
        @DisplayName("디바이스 토큰이 없는 구독자들은 필터링되어 푸시 메시지가 전송되지 않는다")
        fun `should filter out subscribers without device tokens`() {
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
        @DisplayName("활성 구독자와 비활성 구독자가 섞여있으면 활성 구독자에게만 전송한다")
        fun `should send messages only to active subscribers when mixed with inactive ones`() {
            // Given
            val topicId = 1L
            val topic = createTopic(id = 1L, title = "웹 개발")
            val activeUser1 = createUser(id = 1L, deviceToken = "token1")
            val inactiveUser = createUser(id = 2L, deviceToken = null)
            val activeUser2 = createUser(id = 3L, deviceToken = "token3")
            val topicWithSubscribers =
                TopicWithSubscribers(
                    topic = topic,
                    subscribers = listOf(activeUser1, inactiveUser, activeUser2),
                )

            val messagesSlot = slot<List<PushMessage>>()

            every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
            justRun { messageSender.sendMessages(capture(messagesSlot)) }
            every { pushLogRepository.saveAll<PushLog>(any()) } returns listOf()

            // When
            pushMessageSender.sendPushMessages(topicId)

            // Then
            val sentMessages = messagesSlot.captured
            assertThat(sentMessages).hasSize(2)
            assertThat(sentMessages.map { it.userId }).containsExactlyInAnyOrder(1L, 3L)
            assertThat(sentMessages.map { it.deviceToken }).containsExactlyInAnyOrder("token1", "token3")
        }

        @Test
        @DisplayName("메시지 전송과 로그 저장이 올바른 순서로 실행된다")
        fun `should execute message sending and logging in correct order`() {
            // Given
            val topicId = 1L
            val topic = createTopic(id = 1L, title = "모바일 개발")
            val activeUser = createUser(id = 1L, deviceToken = "token1")
            val topicWithSubscribers =
                TopicWithSubscribers(
                    topic = topic,
                    subscribers = listOf(activeUser),
                )

            val callOrder = mutableListOf<String>()

            every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
            justRun {
                messageSender.sendMessages(any())
                callOrder.add("sendMessages")
            }
            every {
                pushLogRepository.saveAll<PushLog>(any())
                callOrder.add("saveAll")
                listOf<PushLog>()
            } returns listOf()

            // When
            pushMessageSender.sendPushMessages(topicId)

            // Then
            assertThat(callOrder).containsExactly("sendMessages", "saveAll")
        }
    }

    @Nested
    @DisplayName("오류 시나리오 테스트")
    inner class ErrorScenarioTest {
        @Test
        @DisplayName("TopicQueryService에서 예외가 발생하면 예외가 전파된다")
        fun `should propagate exception when TopicQueryService throws exception`() {
            // Given
            val topicId = 1L
            val exception = RuntimeException("Topic not found")

            every { topicQueryService.getTopicWithSubscribers(topicId) } throws exception

            // When & Then
            assertThatThrownBy { pushMessageSender.sendPushMessages(topicId) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("Topic not found")

            verify(exactly = 1) { topicQueryService.getTopicWithSubscribers(topicId) }
            verify(exactly = 0) { messageSender.sendMessages(any<List<PushMessage>>()) }
            verify(exactly = 0) { pushLogRepository.saveAll<PushLog>(any<List<PushLog>>()) }
        }

        @Test
        @DisplayName("MessageSender에서 예외가 발생하면 예외가 전파되고 로그는 저장되지 않는다")
        fun `should propagate exception when MessageSender throws exception and not save logs`() {
            // Given
            val topicId = 1L
            val topic = createTopic(id = 1L, title = "클라우드")
            val activeUser = createUser(id = 1L, deviceToken = "token1")
            val topicWithSubscribers =
                TopicWithSubscribers(
                    topic = topic,
                    subscribers = listOf(activeUser),
                )
            val exception = RuntimeException("Failed to send message")

            every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
            every { messageSender.sendMessages(any()) } throws exception

            // When & Then
            assertThatThrownBy { pushMessageSender.sendPushMessages(topicId) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("Failed to send message")

            verify(exactly = 1) { topicQueryService.getTopicWithSubscribers(topicId) }
            verify(exactly = 1) { messageSender.sendMessages(any<List<PushMessage>>()) }
            verify(exactly = 0) { pushLogRepository.saveAll<PushLog>(any<List<PushLog>>()) }
        }

        @Test
        @DisplayName("PushLogRepository에서 예외가 발생하면 예외가 전파된다")
        fun `should propagate exception when PushLogRepository throws exception`() {
            // Given
            val topicId = 1L
            val topic = createTopic(id = 1L, title = "보안")
            val activeUser = createUser(id = 1L, deviceToken = "token1")
            val topicWithSubscribers =
                TopicWithSubscribers(
                    topic = topic,
                    subscribers = listOf(activeUser),
                )
            val exception = RuntimeException("Database connection failed")

            every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
            justRun { messageSender.sendMessages(any()) }
            every { pushLogRepository.saveAll<PushLog>(any()) } throws exception

            // When & Then
            assertThatThrownBy { pushMessageSender.sendPushMessages(topicId) }
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("Database connection failed")

            verify(exactly = 1) { topicQueryService.getTopicWithSubscribers(topicId) }
            verify(exactly = 1) { messageSender.sendMessages(any<List<PushMessage>>()) }
            verify(exactly = 1) { pushLogRepository.saveAll<PushLog>(any<List<PushLog>>()) }
        }
    }

    @Nested
    @DisplayName("데이터 변환 테스트")
    inner class DataTransformationTest {
        @Test
        @DisplayName("Topic과 User 정보가 PushMessage로 올바르게 변환된다")
        fun `should correctly transform Topic and User to PushMessage`() {
            // Given
            val topicId = 1L
            val topic = createTopic(id = 1L, title = "인공지능 개발")
            val user = createUser(id = 2L, deviceToken = "device123")
            val topicWithSubscribers =
                TopicWithSubscribers(
                    topic = topic,
                    subscribers = listOf(user),
                )

            val messagesSlot = slot<List<PushMessage>>()

            every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
            justRun { messageSender.sendMessages(capture(messagesSlot)) }
            every { pushLogRepository.saveAll<PushLog>(any()) } returns listOf()

            // When
            pushMessageSender.sendPushMessages(topicId)

            // Then
            val message = messagesSlot.captured[0]
            assertThat(message.topicId).isEqualTo(1L)
            assertThat(message.eventId).isEqualTo(100L) // 마지막 이벤트 ID
            assertThat(message.userId).isEqualTo(2L)
            assertThat(message.deviceToken).isEqualTo("device123")
            assertThat(message.title).isEqualTo("새로운 후속기사가 도착했어요")
            assertThat(message.content).isEqualTo("인공지능 개발의 후속기사를 보려면 클릭")
        }

        @Test
        @DisplayName("PushMessage가 PushLog로 올바르게 변환된다")
        fun `should correctly transform PushMessage to PushLog`() {
            // Given
            val topicId = 1L
            val topic = createTopic(id = 1L, title = "게임 개발")
            val user = createUser(id = 3L, deviceToken = "game_token")
            val topicWithSubscribers =
                TopicWithSubscribers(
                    topic = topic,
                    subscribers = listOf(user),
                )

            val logsSlot = slot<List<PushLog>>()

            every { topicQueryService.getTopicWithSubscribers(topicId) } returns topicWithSubscribers
            justRun { messageSender.sendMessages(any()) }
            every { pushLogRepository.saveAll<PushLog>(capture(logsSlot)) } returns listOf()

            // When
            pushMessageSender.sendPushMessages(topicId)

            // Then
            val log = logsSlot.captured[0]
            assertThat(log.userId).isEqualTo(3L)
            assertThat(log.token).isEqualTo("game_token")
            assertThat(log.messageTitle).isEqualTo("새로운 후속기사가 도착했어요")
            assertThat(log.messageBody).isEqualTo("게임 개발의 후속기사를 보려면 클릭")
            assertThat(log.sentAt).isNotNull()
        }
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
