package com.devahmed.topito.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devahmed.topito.models.Place
import com.devahmed.topito.services.PlacesService
import com.devahmed.topito.utils.FireStoreStatus
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _places = MutableLiveData<List<Place>>()
    val places: LiveData<List<Place>> = _places

    private val _status = MutableLiveData<FireStoreStatus>()
    val status : LiveData<FireStoreStatus> = _status

    init {
        if(_places.value == null){
            getAllPlaces()
        }
    }

    fun getAllPlaces() {
        viewModelScope.launch {
            _status.value = FireStoreStatus.LOADING
            _places.value = PlacesService.getAllPlaces()
            _status.value = FireStoreStatus.DONE
        }
    }
}