package com.sd.showproducts.repository

import com.sd.showproducts.api.ApiService
import com.sd.showproducts.dto.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
):Repository {

    override suspend fun loadData(limit: Int, skip: Int): MutableList<Product>? {
        try{
            val response = apiService.loadData(limit, skip)
            if (!response.isSuccessful) {
                return mutableListOf()
            }
            val body = response.body()
            return body?.products
        } catch (e:Exception){
            return mutableListOf()
        }
    }

    override suspend fun search(newText: String): MutableList<Product>? {
        try{
            val response = apiService.getProductsFromSearch(newText)
            if (!response.isSuccessful) {
                return mutableListOf()
            }
            val body = response.body()
            return body?.products
        } catch (e:Exception){
            return mutableListOf()
        }
    }

}