package com.team.antiplagiat.repository

import com.team.antiplagiat.models.Solution
import com.team.antiplagiat.models.User
import com.team.antiplagiat.models.Problem
import org.springframework.data.jpa.repository.JpaRepository

interface SolutionRepository : JpaRepository<Solution, Long> {
    fun findAllByUserId(userId: Long): List<Solution>
    fun countByUserAndProblem(user: User, problem: Problem): Long
}
