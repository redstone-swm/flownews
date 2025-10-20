package com.flownews.api.user.domain

import BaseEntity
import com.flownews.api.user.domain.enums.Gender
import com.flownews.api.user.domain.enums.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "oauth_id")
    val oauthId: String,
    @Column(name = "provider")
    val provider: String,
    @Column(name = "name")
    val name: String,
    @Column(name = "email")
    val email: String,
    @Column(name = "profile_url")
    val profileUrl: String? = null,
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    val role: Role,
    @Column(name = "device_token")
    var deviceToken: String? = null,
    @Column(name = "birth_date")
    var birthDate: LocalDate? = null,
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,
    @Column(name = "delete_reason")
    var deleteReason: String? = null,
    @Column(name = "is_profile_complete")
    var isProfileComplete: Boolean = false,
) : BaseEntity() {
    fun requireId(): Long = id ?: throw IllegalStateException("User ID cannot be null")

    fun updateDeviceToken(newDeviceToken: String?) {
        if (newDeviceToken == null) return

        this.deviceToken = newDeviceToken
    }

    fun updateProfile(
        birthDate: LocalDate?,
        gender: Gender?,
    ) {
        this.birthDate = birthDate
        this.gender = gender
        this.isProfileComplete = true
    }

    fun withdraw(reason: String?) {
        this.deletedAt = LocalDateTime.now()
        this.deleteReason = reason
    }
}
