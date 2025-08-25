package com.flownews.api.user.app

class UserCreateValidator {
    fun validate(request: UserCreateRequest) {
        require(request.oauthId.isNotBlank()) { "oauthId must not be blank" }
        require(request.email.isNotBlank()) { "email must not be blank" }
        require(request.name.isNotBlank()) { "name must not be blank" }
    }
}
