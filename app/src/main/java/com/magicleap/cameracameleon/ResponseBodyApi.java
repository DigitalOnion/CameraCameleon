package com.magicleap.cameracameleon;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ResponseBodyApi {

    @GET
    Call<ResponseBody> download(@Url String fileUrl);
}
