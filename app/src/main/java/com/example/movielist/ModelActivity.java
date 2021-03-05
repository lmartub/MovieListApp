package com.example.movielist;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ModelActivity {

    /**
     * Function isNetworkAvalaible to know if network
     * @param connectivityManager object to access information network
     * @return true is available or false not
     */
    public boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
