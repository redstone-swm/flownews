package com.flownews.config.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JwtService {
    private val secretKey: SecretKey
    private val validityInMilliseconds: Long = 3600000 // 1시간

    init {
        val secret = System.getenv("JWT_SECRET")
            ?: throw IllegalStateException("JWT_SECRET 환경변수가 설정되어 있지 않습니다.")
        val decodedKey = Base64.getDecoder().decode(secret)
        secretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, SignatureAlgorithm.HS256.jcaName)
    }

    fun createToken(id: String): String {
        val now = Date()
        val validity = Date(now.time + validityInMilliseconds)
        return Jwts.builder()
            .setSubject(id)
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

    fun getId(token: String): Long? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            claims.body.subject?.toLongOrNull()
        } catch (e: Exception) {
            null
        }
    }
}
