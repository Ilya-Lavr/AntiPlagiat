package com.team.antiplagiat.service

import com.team.antiplagiat.config.props.SolutionConfig
import com.team.antiplagiat.models.Solution
import com.team.antiplagiat.repository.SolutionRepository
import com.team.antiplagiat.repository.UserRepository
import com.team.antiplagiat.repository.ProblemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import io.github.oshai.kotlinlogging.KotlinLogging
import java.time.LocalDateTime

@Service
@Transactional
class SolutionService(
    private val solutionRepository: SolutionRepository,
    private val userRepository: UserRepository,
    private val problemRepository: ProblemRepository,
    private val properties: SolutionConfig
) {
    private val logger = KotlinLogging.logger {}

    fun create(userId: Long, problemId: Long, language: String, filePath: String, code: String?): Solution? {
        logger.info { "Попытка создать решение: userId=$userId, problemId=$problemId" }

        val user = userRepository.findById(userId).orElse(null) ?: run {
            logger.warn { "Пользователь $userId не найден" }
            return null
        }
        val problem = problemRepository.findById(problemId).orElse(null) ?: run {
            logger.warn { "Задача $problemId не найдена" }
            return null
        }

        val attempts = solutionRepository.countByUserAndProblem(user, problem)
        if (attempts >= properties.maxAttempts) {
            logger.warn { "Превышен лимит попыток ${properties.maxAttempts} для user=${user.id}, problem=${problem.id}" }
            return null
        }

        val solution = Solution(
            user = user,
            problem = problem,
            language = language,
            status = "waiting",
            submittedAt = LocalDateTime.now(),
            filePath = filePath,
            code = code
        )
        return solutionRepository.save(solution).also {
            logger.info { "Решение создано: id=${it.id}" }
        }
    }

    fun findById(id: Long): Solution? = solutionRepository.findById(id).orElse(null)

    fun findAll(): List<Solution> = solutionRepository.findAll()

    fun findByUser(userId: Long): List<Solution> = solutionRepository.findAllByUserId(userId)

    fun updateStatus(id: Long, status: String): Solution? {
        val solution = findById(id) ?: return null
        solution.status = status
        return solutionRepository.save(solution)
    }

    fun delete(id: Long) {
        logger.info { "Удаление решения id=$id" }
        solutionRepository.deleteById(id)
    }
}


