package com.sd.showproducts.repository

import com.sd.showproducts.api.ApiService
import com.sd.showproducts.dto.Product
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val apiService: ApiService,
):Repository {

    override suspend fun loadData(limit: Int, skip: Int): List<Product>? {

        try{
            val response = apiService.loadData(limit, skip)
            if (!response.isSuccessful) {
              //  throw ApiError(response.code(), response.message())
                return null
            }
            val body = response.body()
            return body?.products

        } catch (e:Exception){
            return null
        }
    }

}