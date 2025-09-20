package com.flownews.api.user.domain

import BaseEntity
import com.flownews.api.user.domain.enums.Gender
import com.flownews.api.user.domain.enums.Role
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate

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
    val birthDate: LocalDate? = null,
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    val gender: Gender? = null,
) : BaseEntity() {
    fun requireId(): Long = id ?: throw IllegalStateException("User ID cannot be null")
    
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookmarks: MutableList<com.flownews.api.bookmark.domain.Bookmark> = mutableListOf()
}
