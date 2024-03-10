package com.sd.showproducts.repository

import com.sd.showproducts.dto.Product

interface Repository {
    suspend fun loadData(limit: Int, skip: Int): List<Product>?

}