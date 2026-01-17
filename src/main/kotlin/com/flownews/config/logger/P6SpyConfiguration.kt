package com.flownews.config.logger

import com.p6spy.engine.spy.P6SpyOptions
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class P6SpyConfiguration {
    @PostConstruct
    fun setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().logMessageFormat = P6SpySqlFormatter::class.java.name
    }
}
