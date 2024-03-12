package com.sd.showproducts.model

data class ModelCategories(
    val categories: MutableList<String> = mutableListOf(),
    val loading: Boolean = false,
    val error: Boolean = false
)
