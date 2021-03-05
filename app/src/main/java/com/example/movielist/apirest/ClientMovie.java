package com.example.movielist.apirest;

import com.example.movielist.Constant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *  Class that implement client to get information movies.
 */
public class ClientMovie {

    public static Retrofit retrofit = null;

    public static Retrofit getClientMovie(){

        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
