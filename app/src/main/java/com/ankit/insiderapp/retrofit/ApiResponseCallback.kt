package com.ankit.insiderapp.retrofit

import retrofit2.Response

interface ApiResponseCallback {

    /**
     * serviceCallId : API_CONSTANT with which the request was made
    **/
    fun<T> onSuccessCallback(response: Response<T>, serviceCallId:Int)
    fun onFailureCallback(errorMsg: String?,serviceCallId:Int)
}