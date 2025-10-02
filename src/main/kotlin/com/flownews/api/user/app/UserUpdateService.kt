package com.flownews.api.user.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.UserRepository
import com.flownews.api.user.infra.UserProfileApiClient
import com.flownews.api.user.infra.UserProfileVectorUpdateRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserUpdateService(
    private val userRepository: UserRepository,
    private val userProfileClient: UserProfileApiClient,
) {
    @Transactional
    fun updateDeviceToken(
        userId: Long,
        deviceToken: String?,
    ): User {
        val user = getUser(userId)
        user.updateDeviceToken(deviceToken)

        return userRepository.save(user)
    }

    @Transactional
    fun updateProfile(request: UserProfileUpdateRequest): User {
        val (birthDate, gender, userId, topicIds) = request
        val user = getUser(userId)
        user.updateProfile(birthDate, gender)

        userProfileClient.updateUserProfileVector(
            UserProfileVectorUpdateRequest(
                userId = userId,
                topicIds = topicIds,
            ),
        )

        return userRepository.save(user)
    }

    private fun getUser(userId: Long): User =
        userRepository.findById(userId).orElseThrow {
            NoDataException("user not found: $userId")
        }
}
