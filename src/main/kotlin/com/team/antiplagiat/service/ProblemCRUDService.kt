package com.team.antiplagiat.service

import com.team.antiplagiat.models.Problem
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong

@Service
class ProblemCrudService : ServiceCRUD<Problem> {

    private val logger = LoggerFactory.getLogger(ProblemCrudService::class.java)
    override val entities: MutableMap<Long, Problem> = mutableMapOf()
    private val idCounter = AtomicLong(1)

    // CREATE - создать новую задачу
    fun createProblem(name: String, description: String? = null): Boolean {
        logger.info("Попытка создать задачу: '$name'")

        // Генерируем ID
        val id = idCounter.getAndIncrement()
        val problem = Problem(id, name, description)

        // Используем базовую реализацию из ServiceCRUD
        val result = create(problem)

        if (result) {
            logger.info("Задача создана успешно: ID=$id, '$name'")
        } else {
            logger.warn("Не удалось создать задачу: '$name'")
        }

        return result
    }

    // READ - получить все задачи
    fun getAllProblems(): List<Problem> {
        logger.info("Получение всех задач. Всего: ${entities.size}")
        return entities.values.toList()
    }

    // UPDATE - обновить задачу
    fun updateProblem(id: Long, name: String?, description: String? = null): Boolean {
        logger.info("Попытка обновить задачу ID=$id")

        val problem = entities[id] ?: return false

        val updatedProblem = problem.copy(
            name = name ?: problem.name,
            description = description ?: problem.description
        )

        // Удаляем старую и добавляем обновленную
        entities.remove(id)
        entities[id] = updatedProblem

        logger.info("Задача обновлена: ID=$id, '${problem.name}' → '$name'")
        return true
    }

    // DELETE - удалить задачу
    override fun delete(id: Long): Boolean {
        logger.info("Попытка удалить задачу ID=$id")

        val problemName = entities[id]?.name
        val result = super.delete(id)

        if (result) {
            logger.info("Задача удалена: ID=$id, название='$problemName'")
        } else {
            logger.warn("Удаление не удалось: задача с ID=$id не найдена")
        }

        return result
    }

    // Получить количество задач
    fun getProblemCount(): Int = entities.size
}