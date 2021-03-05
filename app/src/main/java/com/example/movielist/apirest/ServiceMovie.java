package com.example.movielist.apirest;

import com.example.movielist.Model.MoviesResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ServiceMovie {
     @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@QueryMap Map<String, String> options);
}
