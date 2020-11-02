package com.ankit.insiderapp.retrofit

import com.ankit.insiderapp.model.ApiResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.collections.HashMap

class RetrofitCallbackManager (private var apiInterface: ApiInterface?, private var apiResponseCallback: ApiResponseCallback?, private var fieldMap: HashMap<String, String>?, private var apiProviderConstants: Int) {

    private fun<T> sendCallbacks(call:Call<T>){
        call.enqueue(object : Callback<T>{
            override fun onResponse(call: Call<T>, response: Response<T>) {
                apiResponseCallback?.onSuccessCallback(response, apiProviderConstants)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                apiResponseCallback?.onFailureCallback(t.message,apiProviderConstants)
            }
        })
    }



    //add all apiCallMethods
    fun homePageData(){
        try {
            val call: Call<ApiResponseModel?>? = apiInterface?.homePageData(fieldMap!!)
            call?.let { sendCallbacks(it) }
        } catch (e:Exception) {
            apiResponseCallback?.onFailureCallback(e.message,apiProviderConstants)
        }
    }
}