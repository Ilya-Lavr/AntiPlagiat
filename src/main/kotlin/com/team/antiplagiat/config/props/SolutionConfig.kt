package com.team.antiplagiat.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import kotlin.properties.Delegates

@ConfigurationProperties(prefix = "antiplagiat")
class SolutionConfig {
    var maxAttempts by Delegates.notNull<Int>()
}