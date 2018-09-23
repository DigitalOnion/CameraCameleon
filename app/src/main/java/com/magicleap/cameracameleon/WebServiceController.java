package com.magicleap.cameracameleon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceController implements Callback<ResponseBody> {

    private String base_url;
    private WebServiceEvents eventHandler;

    public WebServiceController(WebServiceEvents eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void start(String base_url) {
        this.base_url = base_url;
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ResponseBodyApi service = retrofit.create(ResponseBodyApi.class);
        Call<ResponseBody> call = service.download();

        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }
}