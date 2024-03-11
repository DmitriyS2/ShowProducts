package com.sd.showproducts.repository

import com.sd.showproducts.dto.Product

interface Repository {
    suspend fun loadData(limit: Int, skip: Int): MutableList<Product>
    suspend fun search(newText: String): MutableList<Product>
    suspend fun loadAllCategories(): MutableList<String>
    suspend fun loadCurrentCategory(name: String): MutableList<Product>

}