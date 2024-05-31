package com.sd.showproducts.model

import com.sd.showproducts.dto.Category

data class ModelCategories(
    val categories: List<Category> = listOf(),
    val loading: Boolean = false,
    val error: Boolean = false
)
