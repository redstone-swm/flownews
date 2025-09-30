package com.flownews.api.user.app

import com.flownews.api.common.app.NoDataException
import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.UserRepository
import com.flownews.api.user.domain.enums.Gender
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

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

    @Transactional
    fun updateProfile(
        userId: Long,
        birthDate: LocalDate,
        gender: Gender,
    ): User {
        val user =
            userRepository
                .findById(userId)
                .orElseThrow { NoDataException("user not found: $userId") }

        user.birthDate = birthDate
        user.gender = gender
        user.isProfileComplete = true

        return userRepository.save(user)
    }
}
