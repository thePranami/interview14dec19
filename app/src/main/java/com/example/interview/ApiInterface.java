package com.example.interview;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("index.php")
    @FormUrlEncoded
    Call<PojoModel> getAllData(@Field("API") String api);
}
