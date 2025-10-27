package com.example.grayview.services;

import com.example.grayview.models.ResultadoPixabay;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIservice {
    @GET(".")
    Call<ResultadoPixabay> getImages(
            @Query("key") String key,
            @Query("q") String q
    );

    @GET(".")
    Call<ResultadoPixabay> getImages(
            @Query("key") String key,
            @Query("q") String q,
            @Query("image_type") String imageType
    );
}
