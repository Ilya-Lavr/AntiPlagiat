package com.team.antiplagiat

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "problem-service")
data class ProblemServiceConfig(
    // Максимальное количество задач в системе (по умолчанию 100)
    // Значение загружается из application.yaml: problem-service.max-problems
    var maxProblems: Int = 100,

    // Список запрещенных слов в названиях задач
    // Значение загружается из application.yaml: problem-service.forbidden-names
    var forbiddenNames: List<String> = emptyList(),
)