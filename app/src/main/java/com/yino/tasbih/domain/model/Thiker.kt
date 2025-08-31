package com.yino.tasbih.domain.model

import java.time.LocalDateTime
import java.util.Date

data class Thiker(
    val id: Long = 0,
    val title: String = "",
    val maxCount: Long = 0,
    val currentCount: Long = 0,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    var orderIndex: Int = 0,
    val isSelected: Boolean = false,
)
