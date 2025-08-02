package com.flownews.api.topic.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicSectionRepository : CrudRepository<TopicSection, Long>
