package com.flownews.api.user.app

import com.flownews.api.user.domain.User
import com.flownews.api.user.domain.UserRepository
import com.flownews.api.user.domain.enums.Role
import org.springframework.stereotype.Service

@Service
class UserCreateService(
    private val userRepository: UserRepository,
) {
    private val userCreateValidator = UserCreateValidator()

    fun createNewUser(request: UserCreateRequest): User {
        userCreateValidator.validate(request)
        val user = convert(request)
        return userRepository.save(user)
    }

    fun convert(request: UserCreateRequest) =
        User(
            oauthId = request.oauthId,
            email = request.email,
            name = request.name,
            profileUrl = request.profileUrl,
            provider = request.provider.name,
            role = Role.USER,
        )
}
