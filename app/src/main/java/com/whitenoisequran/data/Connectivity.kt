package com.whitenoisequran.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/** True when a connection that actually reaches the internet is up. */
fun Context.isOnline(): Boolean {
    val connectivity = getSystemService(ConnectivityManager::class.java)
    return connectivity.getNetworkCapabilities(connectivity.activeNetwork)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
}
