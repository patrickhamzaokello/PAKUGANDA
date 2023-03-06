package com.example.pakuganda.Apis;


import com.example.pakuganda.Models.HomeFeed;
import com.example.pakuganda.Models.UserAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiEndPoints {




    //post user third part auth details
    @POST("userAuth.php")
    @Headers("Cache-Control: no-cache")
    Call<UserAuth> postAuth(
            @Body UserAuth userAuth
    );

    @GET("near_me.php")
    Call<HomeFeed> getAllInfrastructure(
            @Query("page") int pageIndex
    );

}