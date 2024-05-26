package com.sd.showproducts.repository

import com.sd.showproducts.api.ApiService
import com.sd.showproducts.dto.Category
import com.sd.showproducts.dto.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : Repository {

    override suspend fun loadData(limit: Int, skip: Int): List<Product> {
        try {
            val response = apiService.loadData(limit, skip)
            if (!response.isSuccessful) {
                return listOf()
            }
            val body = response.body()
            return body?.products ?: listOf()
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf()
        }
    }

    override suspend fun search(newText: String): List<Product> {
        try {
            val response = apiService.getProductsFromSearch(newText)
            if (!response.isSuccessful) {
                return listOf()
            }
            val body = response.body()
            return body?.products ?: listOf()
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf()
        }
    }

    override suspend fun loadAllCategories(): List<Category> {
        try {
            val response = apiService.loadAllCategories()
            if (!response.isSuccessful) {
                return mutableListOf()
            }
            return response.body() ?: listOf()
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf()
        }
    }

    override suspend fun loadCurrentCategory(name: String): List<Product> {
        try {
            val response = apiService.loadCurrentCategory(name)
            if (!response.isSuccessful) {
                return listOf()
            }
            val body = response.body()
            return body?.products ?: listOf()
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf()
        }
    }

}