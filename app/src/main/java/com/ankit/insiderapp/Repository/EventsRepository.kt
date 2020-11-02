package com.ankit.insiderapp.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ankit.insiderapp.helpers.Constants
import com.ankit.insiderapp.helpers.HelperMethods
import com.ankit.insiderapp.model.*
import com.ankit.insiderapp.retrofit.*

import kotlinx.coroutines.*
import retrofit2.Response

class EventsRepository : ApiResponseCallback {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private val parentSuperVisor = SupervisorJob()
    private val groupsLiveDataMap: HashMap<String, MutableLiveData<GroupWiseEventModelList>> by lazy {
        HashMap<String, MutableLiveData<GroupWiseEventModelList>>()
    }
    val masterEventsList: MutableLiveData<ModelEvents> by lazy {
        MutableLiveData<ModelEvents>()
    }
    val homePageLiveData: MutableLiveData<HomePageModel> by lazy {
        MutableLiveData<HomePageModel>()
    }

    fun fetchAllEvents(city: String?) {
        ApiServiceProvider().enqueueApiCall(
                UrlContainer.BASE_URL,
                this,
                HelperMethods.prepareFetchEventsHashMap(city)
        ).homePageData()
    }

    private fun handleFailure(msg: String?) {
        val eventsModel = ModelEvents()
        eventsModel.status = Constants.STATUS_FAILURE
        eventsModel.msg = msg ?: "Some error occurred"
        masterEventsList.value = eventsModel
    }

    private fun handleSuccess(responseModelEvents: ApiResponseModel) {
        val eventsModel = ModelEvents()

        when (val linkedHashMapOfEvents = responseModelEvents.list?.masterList) {
            null -> {
                eventsModel.status = Constants.STATUS_NO_DATA
                eventsModel.msg = "no data exist"
            }
            else -> {
                if (linkedHashMapOfEvents.size > 0) {
                    eventsModel.status = Constants.STATUS_SUCCESS
                    eventsModel.msg = "success"
                    eventsModel.responseModel = responseModelEvents
                    setupGroupMap(responseModelEvents)

                } else {
                    eventsModel.status = Constants.STATUS_NO_DATA
                    eventsModel.msg = "no data exist"
                }
            }
        }

        masterEventsList.value = eventsModel
    }

    private fun setupGroupMap(responseModelEvents: ApiResponseModel) {
        setupHomePage(responseModelEvents)
        if (responseModelEvents.groups != null && responseModelEvents.groups.size > 0) {
            responseModelEvents.groups.forEach {
                if (!groupsLiveDataMap.containsKey(it))
                    groupsLiveDataMap[it] = MutableLiveData()
            }
            scope.launch(parentSuperVisor) {
                setupGroupWiseList(responseModelEvents)
            }
        }
    }

    private fun setupHomePage(responseModelEvents: ApiResponseModel) {
        homePageLiveData.postValue(
                HomePageModel(
                        responseModelEvents.popularEventList,
                        responseModelEvents.featuredEventList,
                        responseModelEvents.bannersList
                )
        )
    }

    private fun setupGroupWiseList(responseModelEvents: ApiResponseModel) {
        responseModelEvents.list?.groupList?.let { groupMap ->
            responseModelEvents.groups?.forEach { groupName ->
                val modelList: ArrayList<MasterListEventModel> = ArrayList()
                if (groupMap.containsKey(groupName)) {
                    val keyList = groupMap[groupName]
                    keyList?.forEach { groupkey ->
                        responseModelEvents.list.masterList?.let {
                            if (it.containsKey(groupkey) && it[groupkey] != null)
                                it[groupkey]?.let { eventModel ->
                                    modelList.add(eventModel)
                                }
                        }
                    }
                }
                groupsLiveDataMap[groupName]?.postValue(
                        GroupWiseEventModelList(
                                modelList
                        )
                )
            }
        }
    }

    override fun <T> onSuccessCallback(response: Response<T>, serviceCallId: Int) {
        val responseModelEvents = response.body() as? ApiResponseModel
        if (responseModelEvents == null) {
            handleFailure("body is empty")
        } else {
            handleSuccess(responseModelEvents)
        }
    }

    override fun onFailureCallback(errorMsg: String?, serviceCallId: Int) {
        handleFailure(errorMsg)
    }

    fun getEventsForGroup(groupKey: String): LiveData<GroupWiseEventModelList>? {
        return groupsLiveDataMap[groupKey]
    }

    fun stopProcesses() {
        parentSuperVisor.cancelChildren()
    }
}