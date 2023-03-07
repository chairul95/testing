package com.example.pacinterviewtesting;

import android.provider.Settings;

import com.example.pacinterviewtesting.model.ResponseData;
import com.example.pacinterviewtesting.model.detail.ResponseDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("products")
    Call<ArrayList<ResponseData>> getDataReq();

    @GET("{ID}")
    Call<ResponseDetail> getDataDetailReq(@Path("ID") int userId);
}
