package com.team.antiplagiat.service

import com.team.antiplagiat.SolutionConfig
import com.team.antiplagiat.models.Solution
import org.springframework.stereotype.Service

@Service
class SolutionCRUD(
    private val properties: SolutionConfig
) : ServiceCRUD<Solution> {

    override val entities = mutableMapOf<Long, Solution>()

    private val attemptsCounter = mutableMapOf<Pair<Long, Long>, Int>()

    override fun create(entity: Solution): Boolean {
        println("Попытка создать посылку: $entity")

        val key = entity.userId to entity.taskId
        val attempts = attemptsCounter.getOrDefault(key, 0)

        if (attempts >= properties.maxAttempts) {
            println(
                "Превышено кол-во попыток ${properties.maxAttempts} для userId ${entity.userId}, taskId ${entity.taskId}"
            )
            return false
        }

        val created = super.create(entity)
        if (created) {
            attemptsCounter[key] = attempts + 1
            println(
                "Посылка создана. id ${entity.id}, попытка ${attemptsCounter[key]}/${properties.maxAttempts}"
            )
        } else {
            println("Посылка с id ${entity.id} уже существует")
        }

        return created
    }

    fun update(id: Long, updated: Solution): Boolean {
        if (!entities.containsKey(id)) {
            println("Обновление невозможно, т.к. посылка id $id не найдена")
            return false
        }

        entities[id] = updated
        println("Посылка обновлена, id $id")
        return true
    }
}
