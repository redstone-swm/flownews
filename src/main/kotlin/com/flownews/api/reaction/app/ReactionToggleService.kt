package com.flownews.api.reaction.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.event.domain.EventRepository
import com.flownews.api.reaction.domain.Reaction
import com.flownews.api.reaction.domain.ReactionRepository
import com.flownews.api.reaction.domain.ReactionTypeRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ReactionToggleService(
    private val reactionRepository: ReactionRepository,
    private val reactionTypeRepository: ReactionTypeRepository,
    private val eventRepository: EventRepository,
) {
    fun toggleReaction(
        eventId: Long,
        reactionTypeId: Long,
        user: User,
    ): ReactionToggleResponse {
        val event =
            eventRepository.findById(eventId).orElseThrow {
                NoDataException("Event not found: $eventId")
            }

        val reactionType =
            reactionTypeRepository.findById(reactionTypeId).orElseThrow {
                NoDataException("ReactionType not found: $reactionTypeId")
            }

        // 기존 반응 찾기 (삭제되지 않은 것만)
        val existingActiveReaction =
            reactionRepository.findByUserIdAndEventIdAndIsDeletedIsNull(user.requireId(), eventId)

        return when {
            // 기존 반응이 없는 경우 새로 생성
            existingActiveReaction == null -> {
                val newReaction =
                    Reaction(
                        user = user,
                        event = event,
                        reactionType = reactionType,
                        isDeleted = null,
                    )
                reactionRepository.save(newReaction)
                val count =
                    reactionRepository.countByEventIdAndReactionTypeIdAndIsDeletedIsNull(
                        eventId,
                        reactionTypeId,
                    )
                ReactionToggleResponse(
                    isActive = true,
                    message = "${reactionType.name} 반응이 추가되었습니다.",
                    count = event.viewCount + count,
                )
            }

            // 같은 반응 타입인 경우 해제
            existingActiveReaction.reactionType.id == reactionTypeId -> {
                existingActiveReaction.isDeleted = LocalDateTime.now()
                reactionRepository.save(existingActiveReaction)
                val count =
                    reactionRepository.countByEventIdAndReactionTypeIdAndIsDeletedIsNull(
                        eventId,
                        reactionTypeId,
                    )
                ReactionToggleResponse(
                    isActive = false,
                    message = "${reactionType.name} 반응이 해제되었습니다.",
                    count = event.viewCount + count,
                )
            }

            // 다른 반응 타입인 경우 기존 반응 삭제하고 새로 생성
            else -> {
                existingActiveReaction.isDeleted = LocalDateTime.now()
                reactionRepository.save(existingActiveReaction)

                val newReaction =
                    Reaction(
                        user = user,
                        event = event,
                        reactionType = reactionType,
                        isDeleted = null,
                    )
                reactionRepository.save(newReaction)
                val count =
                    reactionRepository.countByEventIdAndReactionTypeIdAndIsDeletedIsNull(
                        eventId,
                        reactionTypeId,
                    )
                ReactionToggleResponse(
                    isActive = true,
                    message = "${reactionType.name} 반응이 추가되었습니다.",
                    count = count,
                )
            }
        }
    }
}
