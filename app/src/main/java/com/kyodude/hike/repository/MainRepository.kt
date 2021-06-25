package com.kyodude.hike.repository

import com.kyodude.hike.model.api.ApiService
import com.kyodude.hike.model.dataModel.PhotosResponse
import com.kyodude.weatherapp.util.Resource
import javax.inject.Inject

class MainRepository @Inject constructor(private val api:ApiService) : Repository {
    override suspend fun getPhotos(text: String, page: Int): Resource<PhotosResponse> {
        return try {
            val response = api.getPhotos(text, page)
            val result = response.body()
            if(response.isSuccessful && result != null) {
                Resource.Success(result)
            }
            else {
                Resource.Error(response.message())
            }
        }
        catch (e: Exception){
            e.stackTrace
            Resource.Error("An error occurred while fetching photos.")
        }
    }
}