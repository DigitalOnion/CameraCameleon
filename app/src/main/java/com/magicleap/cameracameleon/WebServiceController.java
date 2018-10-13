package com.magicleap.cameracameleon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceController implements Callback<ResponseBody> {

    private String base_url;
    private String end_point;
    private WebServiceEvents eventHandler;

    public WebServiceController(WebServiceEvents eventHandler) {
        this.eventHandler = eventHandler;
    }

    public void start(String baseUrl, String endPoint, String fileUrl) {
        this.base_url = baseUrl;
        this.end_point = endPoint;

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(baseUrl + endPoint)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

        ResponseBodyApi service = retrofit.create(ResponseBodyApi.class);
        Call<ResponseBody> call = service.download(fileUrl);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        byte[] bytes = new byte[0];
        try {
            bytes = (byte[]) response.body().bytes();
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
            eventHandler.onSuccess(bitmapImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        eventHandler.onFailure("I'have found difficulties");
    }
}