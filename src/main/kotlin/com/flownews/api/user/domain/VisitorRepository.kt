package com.flownews.api.user.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface VisitorRepository : CrudRepository<Visitor, UUID>