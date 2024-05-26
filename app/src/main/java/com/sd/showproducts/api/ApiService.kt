package com.sd.showproducts.api

import com.sd.showproducts.dto.Category
import com.sd.showproducts.dto.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun loadData(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): Response<Products>

    @GET("products/search")
    suspend fun getProductsFromSearch(
        @Query("q") search: String
    ): Response<Products>

    @GET("products/categories")
    suspend fun loadAllCategories(): Response<List<Category>>

    @GET("products/category/{name}")
    suspend fun loadCurrentCategory(@Path("name") name: String): Response<Products>
}
