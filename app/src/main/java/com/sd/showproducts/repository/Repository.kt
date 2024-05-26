package com.sd.showproducts.repository

import com.sd.showproducts.dto.Category
import com.sd.showproducts.dto.Product

interface Repository {
    suspend fun loadData(limit: Int, skip: Int): List<Product>
    suspend fun search(newText: String): List<Product>
    suspend fun loadAllCategories(): List<Category>
    suspend fun loadCurrentCategory(name: String): List<Product>

}