package com.team.antiplagiat.controller.dto

import com.team.antiplagiat.models.Solution
import com.team.antiplagiat.models.User
import com.team.antiplagiat.models.Problem
import java.time.LocalDateTime

data class SolutionRequest(
    val userId: Long,
    val problemId: Long,
    val language: String,
    val filePath: String,
    val code: String?
)

data class SolutionResponse(
    val id: Long,
    val userId: Long,
    val problemId: Long,
    val language: String,
    val status: String,
    val submittedAt: LocalDateTime,
    val filePath: String,
    val code: String?
) {
    companion object {
        fun fromEntity(solution: Solution): SolutionResponse = SolutionResponse(
            id = solution.id,
            userId = solution.user.id,
            problemId = solution.problem.id,
            language = solution.language,
            status = solution.status,
            submittedAt = solution.submittedAt,
            filePath = solution.filePath,
            code = solution.code
        )
    }
}

fun SolutionRequest.toEntity(user: User, problem: Problem): Solution = Solution(
    user = user,
    problem = problem,
    language = this.language,
    status = "waiting",
    submittedAt = LocalDateTime.now(),
    filePath = this.filePath,
    code = this.code
)
