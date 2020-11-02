package com.ankit.insiderapp.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.ImageView
import com.ankit.insiderapp.R
import com.bumptech.glide.Glide
import kotlin.collections.HashMap
import kotlin.collections.set

class HelperMethods {
    companion object {
        fun prepareFetchEventsHashMap(city: String?): HashMap<String, String> {
            val hashMap = HashMap<String, String>()
            hashMap[Constants.PARAM_NORM] = "1"
            hashMap[Constants.PARAM_FILTER_BY] = "go-out"
            hashMap[Constants.PARAM_CITY] = city ?: Constants.DEFAULT_CITY
            return hashMap
        }

        fun withPriceOrFree(price: String?): String {
            return if (price?.equals(
                            "0",
                            ignoreCase = false
                    ) == true
            ) "FREE" else "₹${price}"
        }
    }
}

fun Context.iConnectedToNetwork(): Boolean {
    val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        val nwInfo = connectivityManager.activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }
}

fun ImageView.setImageWithGlide(url: String) {
    Glide
            .with(context)
            .load(url)
            .placeholder(R.drawable.bg_default_event)
            .fitCenter()
            .into(this)
}
