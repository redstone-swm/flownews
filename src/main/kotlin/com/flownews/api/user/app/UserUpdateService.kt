package com.flownews.api.user.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserUpdateService(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun updateDeviceToken(request: UserDeviceTokenUpdateRequest): User {
        val (userId, deviceToken) = request
        val user = getUser(userId)
        user.updateDeviceToken(deviceToken)

        return userRepository.save(user)
    }

    @Transactional
    fun updateProfile(request: UserProfileUpdateRequest?, userId: Long): User {
        val birthDate = request?.birthDate
        val gender = request?.gender
        val user = getUser(userId)

        user.updateProfile(birthDate, gender)

        return userRepository.save(user)
    }

    private fun getUser(userId: Long): User =
        userRepository.findById(userId).orElseThrow {
            NoDataException("user not found: $userId")
        }
}
