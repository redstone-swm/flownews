package com.flownews.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtProvider {
    private val secretKey: SecretKey
    private val validityInMilliseconds: Long = 3600000 // 1시간

    init {
        val secret = System.getenv("JWT_SECRET")
            ?: throw IllegalStateException("JWT_SECRET 환경변수가 설정되어 있지 않습니다.")
        val decodedKey = Base64.getDecoder().decode(secret)
        secretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, SignatureAlgorithm.HS256.jcaName)
    }

    fun createTokenWithOauthId(oauthId: String): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)
        return Jwts.builder()
            .setSubject(oauthId)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getOauthId(token: String): String? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            claims.body.subject
        } catch (e: Exception) {
            null
        }
    }
}
