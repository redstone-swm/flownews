package com.flownews.api.bookmark.domain

import org.springframework.data.jpa.repository.JpaRepository

interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    fun findByUserIdAndEventIdAndIsDeletedFalse(userId: Long, eventId: Long): Bookmark?
    fun findByUserIdAndIsDeletedFalse(userId: Long): List<Bookmark>
    fun findByEventIdAndIsDeletedFalse(eventId: Long): List<Bookmark>
    fun countByEventIdAndIsDeletedFalse(eventId: Long): Long
}