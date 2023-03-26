package com.pkasemer.pakuganda.Apis;


import com.pkasemer.pakuganda.Models.MapBase;
import com.pkasemer.pakuganda.Models.NeedsBase;
import com.pkasemer.pakuganda.Models.SelectedVillage;
import com.pkasemer.pakuganda.Models.UserAuth;

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

    @GET("map.php")
    Call<MapBase> getAllInfrastructure(
            @Query("page") int pageIndex
    );

    @GET("singleVillage.php")
    Call<SelectedVillage> getSelectedVillage(
            @Query("villageID") int villageID
    );

    @GET("all_needs.php")
    @Headers("Cache-Control: no-cache")
    Call<NeedsBase> getAllNeeds(
            @Query("page") int pageIndex
    );

}