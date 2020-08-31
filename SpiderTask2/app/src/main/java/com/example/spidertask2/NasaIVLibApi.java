package com.example.spidertask2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaIVLibApi {

    @GET("search")
    Call<ImageAndVideoLibrary> getCollection(@Query("q") String q);

}
