package com.flownews.api.user.domain

import com.flownews.api.user.domain.enums.Role
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Column
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "oauth_id", nullable = false)
    val oauthId: String,

    @Column(name = "provider", nullable = false)
    val provider: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "profile_url", nullable = true)
    val profileUrl: String? = null,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: Role,

    @Column(name = "device_token", nullable = true)
    var deviceToken: String? = null
)
