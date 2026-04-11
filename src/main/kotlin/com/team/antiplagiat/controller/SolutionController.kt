package com.team.antiplagiat.controller

import com.team.antiplagiat.controller.dto.SolutionRequest
import com.team.antiplagiat.controller.dto.SolutionResponse
import com.team.antiplagiat.service.SolutionService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/solutions")
class SolutionController(private val solutionService: SolutionService) {

    @PostMapping
    fun create(@RequestBody request: SolutionRequest): ResponseEntity<SolutionResponse> {
        val solution = solutionService.create(
            userId = request.userId,
            problemId = request.problemId,
            language = request.language,
            filePath = request.filePath,
            code = request.code
        ) ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.status(HttpStatus.CREATED).body(SolutionResponse.fromEntity(solution))
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ResponseEntity<SolutionResponse> {
        val solution = solutionService.findById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SolutionResponse.fromEntity(solution))
    }

    @GetMapping
    fun getAll(): List<SolutionResponse> = solutionService.findAll().map { SolutionResponse.fromEntity(it) }

    @GetMapping("/user/{userId}")
    fun getByUser(@PathVariable userId: Long): List<SolutionResponse> =
        solutionService.findByUser(userId).map { SolutionResponse.fromEntity(it) }

    @PatchMapping("/{id}/status")
    fun updateStatus(@PathVariable id: Long, @RequestParam status: String): ResponseEntity<SolutionResponse> {
        val updated = solutionService.updateStatus(id, status) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SolutionResponse.fromEntity(updated))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        solutionService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
