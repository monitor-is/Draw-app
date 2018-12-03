package com.mlh.mlh.data;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("order/validateToken")
    Call<String> validateToken();

    @Multipart
    @POST("order/setOrderImage")
    Call<ResponseBody> setImage(@Part MultipartBody.Part token, @Part MultipartBody.Part image);

}
