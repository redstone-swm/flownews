package com.flownews.api.event.app

import com.flownews.api.event.domain.Event
import com.flownews.api.event.domain.EventQueryService
import com.flownews.api.event.domain.reaction.Like
import com.flownews.api.event.domain.reaction.LikeRepository
import com.flownews.api.user.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class EventLikeService(
    private val likeRepository: LikeRepository,
    private val eventQueryService: EventQueryService,
) {
    fun toggleLike(
        eventId: Long,
        user: User,
    ) {
        val event = eventQueryService.findEventById(eventId)
        val existingLike = findExistingLike(user.requireId(), eventId)

        return if (existingLike == null) {
            addLike(event, user)
        } else {
            removeLike(existingLike)
        }
    }

    private fun findExistingLike(
        userId: Long,
        eventId: Long,
    ): Like? {
        return likeRepository.findByUserIdAndEventIdAndIsDeletedIsNull(userId, eventId)
    }

    private fun addLike(
        event: Event,
        user: User,
    ) {
        val newLike = Like.of(user, event)
        likeRepository.save(newLike)
    }

    private fun removeLike(like: Like) {
        like.delete()
        likeRepository.save(like)
    }
}
