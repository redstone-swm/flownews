package com.flownews.api.user.app

import com.flownews.api.user.domain.Visitor
import com.flownews.api.user.domain.VisitorRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class VisitorSyncService(private val visitorRepository: VisitorRepository) {

    @Transactional
    fun sync(req: VisitorSyncRequest): Visitor {
        val visitor = req.visitorId?.let { updateVisitor(req) } ?: createVisitor(req)
        return visitorRepository.save(visitor)
    }

    private fun createVisitor(req: VisitorSyncRequest): Visitor {
        return Visitor(UUID.randomUUID(), req.userAgent, req.ipAddress, req.token)
    }

    private fun updateVisitor(req: VisitorSyncRequest): Visitor {
        val visitor = visitorRepository.findById(req.visitorId!!).orElseThrow()
        visitor.update(req.userAgent, req.ipAddress, req.token)

        return visitor
    }
}

data class VisitorSyncRequest(val visitorId: UUID?, val userAgent: String, val ipAddress: String, val token: String?)