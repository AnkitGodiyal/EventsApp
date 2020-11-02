package com.ankit.insiderapp.ViewModelpackage

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ankit.insiderapp.Repository.EventsRepository
import com.ankit.insiderapp.helpers.Constants
import com.ankit.insiderapp.model.GroupWiseEventModelList
import com.ankit.insiderapp.model.HomePageModel
import com.ankit.insiderapp.model.ModelEvents

class MainViewModel : ViewModel() {
    private val eventsRepository by lazy {
        EventsRepository()
    }
    fun fetchAllEvents(city: String? = Constants.DEFAULT_CITY) {
        eventsRepository.fetchAllEvents(city)
    }

    fun getAllEvents(): LiveData<ModelEvents>? {
        return eventsRepository.masterEventsList
    }

    fun getEventsForGroup(groupKey: String): LiveData<GroupWiseEventModelList>? {
        return eventsRepository.getEventsForGroup(groupKey)
    }

    fun getModelForHome(): LiveData<HomePageModel>? {
        return eventsRepository.homePageLiveData
    }

    override fun onCleared() {
        super.onCleared()
        eventsRepository.stopProcesses()
    }
}