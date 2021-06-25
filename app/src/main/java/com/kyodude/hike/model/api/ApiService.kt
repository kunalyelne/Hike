package com.kyodude.hike.model.api

import com.kyodude.hike.model.dataModel.PhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("services/rest")
    suspend fun getPhotos(@Query("text") searchText:String, @Query("page") page: Int): Response<PhotosResponse>
}
