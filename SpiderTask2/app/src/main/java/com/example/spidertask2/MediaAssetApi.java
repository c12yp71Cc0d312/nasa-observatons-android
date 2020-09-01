package com.example.spidertask2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MediaAssetApi {

    @GET("asset/{nasa_id}")
    Call<MediaAsset> getAsset(@Path("nasa_id") String nasa_id);

}
