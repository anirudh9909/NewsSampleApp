package com.example.anirudhbharti.newsapp;

import com.example.anirudhbharti.newsapp.model.NewsResponseModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v2/top-headlines")
    Observable<NewsResponseModel> getNewsResponse(
            @Query("country") String username,
            @Query("apiKey") String password);

}