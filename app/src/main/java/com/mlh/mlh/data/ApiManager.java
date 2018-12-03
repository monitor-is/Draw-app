package com.mlh.mlh.data;

import com.mlh.mlh.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {
    private ApiService service;
    private ApiService jsonService;

    public ApiService getJsonService() {
        if (jsonService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jsonService = retrofit.create(ApiService.class);
        }
        return jsonService;
    }

    public ApiService getService() {
        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.API)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            service = retrofit.create(ApiService.class);
        }
        return service;
    }
}
