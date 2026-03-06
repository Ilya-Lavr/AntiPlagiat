package com.team.antiplagiat.runner

import com.team.antiplagiat.models.user.User
import com.team.antiplagiat.models.user.service.UserCRUD
import com.team.antiplagiat.service.ContestCRUD
import com.team.antiplagiat.service.ProblemCrudService
import org.springframework.boot.CommandLineRunner
import com.team.antiplagiat.models.Contest
import com.team.antiplagiat.models.Solution
import com.team.antiplagiat.service.SolutionCRUD
import java.time.LocalDateTime
import org.springframework.stereotype.Component

@Component
class CommandLineRunner(
    private val problemService: ProblemCrudService,
    private val userService: UserCRUD,
    private val contestService: ContestCRUD,
    private val solutionService: SolutionCRUD
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        println("=".repeat(70))
        println("ДЕМОНСТРАЦИЯ РАБОТЫ CRUD СЕРВИСОВ")
        println("=".repeat(70))

        println("\nЧАСТЬ 1: ДЕМОНСТРАЦИЯ UserCRUD")
        println("-".repeat(30))


        println("\n1. СОЗДАНИЕ ПОЛЬЗОВАТЕЛЕЙ:")
        val user1 = User(1, "user1", "user1@example.com")
        val user2 = User(2, "user2", "user2@example.com")

        userService.create(user1)
        userService.create(user2)


        println("\n2. ЧТЕНИЕ ПОЛЬЗОВАТЕЛЯ:")
        val foundUser = userService.read(1)
        println("Найден пользователь: ${foundUser?.login} (email: ${foundUser?.email})")

        println("\n3. ОБНОВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ:")
        userService.update(1, "updated_user", "new_email@example.com")


        val updatedUser = userService.read(1)
        println("Обновленный email: ${updatedUser?.email}")

        println("\n4. УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ:")
        userService.delete(2)

        println("\nЧАСТЬ 2: ДЕМОНСТРАЦИЯ ProblemCrudService")
        println("-".repeat(30))

        println("\n1. СОЗДАНИЕ ЗАДАЧ:")
        problemService.createProblem("Сумма чисел", "Найти сумму двух чисел")
        problemService.createProblem("Факториал", "Вычислить факториал числа")
        problemService.createProblem("Палиндром", "Проверить строку на палиндром")

        println("\n2. ЧТЕНИЕ ВСЕХ ЗАДАЧ:")
        val allProblems = problemService.getAllProblems()
        println("Всего задач: ${allProblems.size}")
        allProblems.forEach { problem ->
            println("  ID: ${problem.id}, Название: '${problem.name}'")
        }

        println("\n3. ЧТЕНИЕ ЗАДАЧИ ПО ID:")
        val problem = problemService.read(1)
        println("Задача с ID=1: ${problem?.name} - ${problem?.description}")

        println("\n4. ОБНОВЛЕНИЕ ЗАДАЧИ:")
        problemService.updateProblem(1, "Сумма двух чисел", "Сложить 2 числа")


        println("\n5. УДАЛЕНИЕ ЗАДАЧИ:")
        problemService.delete(2)

        println("\n6. ФИНАЛЬНОЕ СОСТОЯНИЕ:")
        val finalProblems = problemService.getAllProblems()
        println("Осталось задач: ${finalProblems.size}")

        println("\n" + "=".repeat(70))
        println("ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА")
        println("=".repeat(70))



        println("\nЧАСТЬ 3: ДЕМОНСТРАЦИЯ ContestCRUD")
        println("-".repeat(30))

        println("\n1. СОЗДАНИЕ КОНТЕСТОВ:")
        val contest1 = Contest(1, "A", 100, LocalDateTime.now().plusDays(7), 3 * 3600)
        val contest2 = Contest(2, "О", 200, LocalDateTime.now().plusDays(3), 5 * 3600)
        val contest3 = Contest(3, "ПЕ", 100, LocalDateTime.now().plusDays(1), 8 * 3600)
        contestService.create(contest1)
        contestService.create(contest2)
        contestService.create(contest3)
        println("\n2. СОЗДАНИЕ КОНТЕСТОВ ДЛЯ ПОИСКА ПО АДМИНУ:")
        val contest4 = Contest(4, "Контест админа 100", 100, LocalDateTime.now().plusDays(2), 4 * 3600)
        val contest5 = Contest(5, "Контест админа 300", 300, LocalDateTime.now().plusDays(1), 2 * 3600)
        contestService.create(contest4)
        contestService.create(contest5)
        println("\n3. ЧТЕНИЕ КОНТЕСТА ПО ID:")
        val foundContest = contestService.read(1)
        println("Найден контест: ${foundContest?.name} (длительность: ${foundContest?.duration?.div(3600)} часов)")
        println("\n4. ОБНОВЛЕНИЕ КОНТЕСТА:")
        contestService.update(1, "ПЕ", 4 * 3600)
        val updatedContest = contestService.read(1)
        println("Обновленный контест: '${updatedContest?.name}', ${updatedContest?.duration?.div(3600)} часов")
        println("\n5. ПОИСК КОНТЕСТОВ ПО АДМИНУ:")
        val admin100Contests = contestService.getByAdmin(100)
        if (admin100Contests != null) {
            println("Контесты админа 100:")
            admin100Contests.forEach { contest ->
                println("  • '${contest.name}' (${contest.duration / 3600} часов)")
            }
        }
        val admin300Contests = contestService.getByAdmin(300)
        if (admin300Contests != null) {
            println("Контесты админа 300:")
            admin300Contests.forEach { contest ->
                println("  • '${contest.name}'")
            }
        }
        println("\n6. УДАЛЕНИЕ КОНТЕСТА:")
        contestService.delete(4)

        println("\n" + "=".repeat(70))
        println("ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА")
        println("=".repeat(70))

        println("\nЧАСТЬ 4: ДЕМОНСТРАЦИЯ SolutionCRUD")
        println("-".repeat(30))
        val solution = Solution(10, 2, 1, "C++", "waiting", LocalDateTime.now())
        println("\n1. СОЗДАНИЕ ПОСЫЛОКИ:")
        solutionService.create(solution)
        println("\n2. ЛОВИМ ОГРАНИЧЕНИЕ:")
        for (i in 1..51) {
            val solution = Solution(i.toLong(), 2, 1, "C++", "waiting", LocalDateTime.now())
            val res = solutionService.create(solution)
            println("Попытка $i: ${if (res) "УСПЕШНО" else "ОТКЛОНЕНО (превышен лимит)"}")
        }
        println("\n3. ЧТЕНИЕ ПОСЫЛОК:")
        val res = solutionService.read(1)
        println("  ID: ${res?.id}, Пользователь: ${res?.userId}, Задача: ${res?.taskId}")
        println("\n4. ОБНОВЛЕНИЕ ПОСЫЛКИ:")
        val updatedSolution = Solution(
            1,
            1,
            1,
            "Python",
            "waitng",
            LocalDateTime.now(),
        )
        solutionService.update(1, updatedSolution)
        println("\n5. УДАЛЕНИЕ ПОСЫЛОК:")
        solutionService.delete(1)
        solutionService.delete(2)
        println("\n" + "=".repeat(70))
        println("ДЕМОНСТРАЦИЯ ЗАВЕРШЕНА")
        println("=".repeat(70))
    }
}
