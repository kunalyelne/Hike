package com.kyodude.hike.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.kyodude.hike.model.dataModel.Photo
import com.kyodude.hike.repository.Repository
import com.kyodude.weatherapp.util.DispatcherProvider
import com.kyodude.weatherapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mainRepository: Repository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    val TAG = MainActivityViewModel::class.java.simpleName
    private val photoList = MutableLiveData<List<Photo>>()
    private val loading = MutableLiveData<Boolean>(false)
    private var page = 1

    fun incrementPage(query: String) {
        page++
        setSearch(query)
    }

    fun getPhotoListLiveData(): LiveData<List<Photo>> = photoList

    fun getIsLoading(): LiveData<Boolean> = loading

    fun setSearch(query: String) {
        loading.value = true
        viewModelScope.launch(dispatchers.io) {
            when(val response = mainRepository.getPhotos(query, page)) {
                is Resource.Error -> {
                    loading.postValue(false)
                    Log.e(TAG, response.message!!)
                }
                is Resource.Success -> {
                    loading.postValue(false)
                    photoList.postValue(response.data?.photos?.photo)
                }
            }
        }
    }
}