package com.flownews.api.user.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.user.domain.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserUpdateService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun updateDeviceToken(
        userId: Long,
        deviceToken: String?,
    ) {
        val user =
            userRepository
                .findById(userId)
                .orElseThrow { NoDataException("user not found: $userId") }

        user.deviceToken = deviceToken
    }
}
