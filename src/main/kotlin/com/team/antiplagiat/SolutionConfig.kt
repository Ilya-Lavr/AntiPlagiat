package com.team.antiplagiat

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "antiplagiat")
class SolutionConfig {
    var maxAttempts: Int = 50
}