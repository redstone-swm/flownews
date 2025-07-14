package com.flownews.api.logs.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserEventLogger : CrudRepository<UserEventLog, Long>
