package com.example.spidertask2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaApodApi {

    @GET("planetary/apod")
    Call<Apod> getPosts(@Query("api_key") String key,
                              @Query("date") String date,
                              @Query("hd") boolean hd);

}
