package com.flownews.api.user.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long> {
    fun findByOauthIdAndProvider(
        oauthId: String,
        provider: String,
    ): User?
}
