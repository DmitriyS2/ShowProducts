package com.sd.showproducts.model

import com.sd.showproducts.dto.Product

data class ModelProducts(
    val products: MutableList<Product>? = mutableListOf(),
    val loading:Boolean = false,
    val error:Boolean = false
)