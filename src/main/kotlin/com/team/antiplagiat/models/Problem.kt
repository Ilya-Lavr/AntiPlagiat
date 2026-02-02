package com.team.antiplagiat.models

data class Problem(
    override val id: Long,  // ID задачи
    val name: String,       // Название задачи
    val description: String? = null // Описание задачи, может быть null
) : Entity