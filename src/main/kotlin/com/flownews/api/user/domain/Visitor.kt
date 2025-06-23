package com.flownews.api.user.domain

import BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.UUID

@Entity
@Table(name = "visitors")
class Visitor(
    @Id
    @Column(name = "id")
    @JdbcTypeCode(SqlTypes.CHAR)
    val id: UUID?,

    @Column(name = "user_agent")
    var userAgent: String,

    @Column(name = "token")
    var token: String? = null
) : BaseEntity() {
    fun update(userAgent: String, token: String?) {
        this.userAgent = userAgent
        this.token = token
    }
}