package com.flownews.api.push.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PushLogRepository : CrudRepository<PushLog, Long>
