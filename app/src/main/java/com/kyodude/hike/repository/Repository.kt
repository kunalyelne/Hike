package com.kyodude.hike.repository

import com.kyodude.hike.model.dataModel.PhotosResponse
import com.kyodude.weatherapp.util.Resource

interface Repository {
    suspend fun getPhotos(text: String, page: Int): Resource<PhotosResponse>
}