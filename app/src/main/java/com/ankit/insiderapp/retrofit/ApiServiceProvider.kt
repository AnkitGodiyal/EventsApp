package com.ankit.insiderapp.retrofit

import com.ankit.insiderapp.helpers.Constants.Companion.API_CONSTANT_DEFAULT
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Use when you want to make API request
 * @enqueueApiCall method takes apiClient (default apiClient is provided if not received) & retrofitInstance (default instance is provided if not given)
 * @enqueueApiCall returns RetrofitCallbackManager instance
 * with the RetrofitCallbackManager you need to call methods to enqueue your request for the response
 *
 * Flow to make a api request must be
 *  APIServiceProvider ---> enqueueApiCall -----> RetrofitCallbackManager ----> call your respective method to enqueue request
 *  example : ApiServiceProvider().enqueueApiCall(baseUrl,callback,paramHashMap,apiProviderConstant).homePageData
 *
 * You can pass your own apiClient or retrofitInstance to the constructor of ApiServiceProvider by defining them inside OkHttpClientBuilder
 * and RetrofitInstanceBuilder respectively
 *
**/

class ApiServiceProvider (private var apiClient: OkHttpClient? = null,private var retrofit: Retrofit? = null) {

    private var apiInterface:ApiInterface? =null

    fun enqueueApiCall(baseUrl:String, apiResponseCallback: ApiResponseCallback, fieldMap:HashMap<String,String>, apiProviderConstants: Int = API_CONSTANT_DEFAULT): RetrofitCallbackManager {

        var retrofitCallbackManager: RetrofitCallbackManager

        try {

            setApiClientData(baseUrl)

            apiInterface = retrofit!!.create(ApiInterface::class.java)

            retrofitCallbackManager = RetrofitCallbackManager(apiInterface,apiResponseCallback,fieldMap,apiProviderConstants)
        }
        catch (e:Exception){
            e.printStackTrace()
            retrofitCallbackManager = RetrofitCallbackManager(apiInterface,apiResponseCallback,fieldMap,apiProviderConstants)
        }

        return retrofitCallbackManager
    }

    private fun setApiClientData(baseUrl: String) {
        when (retrofit) {
            null -> {
                if(apiClient == null) {
                    apiClient = OkHttpClientBuilder.defaultOkHttpClient
                }
                retrofit = RetrofitInstanceBuilder.getDefaultRetrofit(baseUrl,apiClient!!)
            }
        }
    }
}