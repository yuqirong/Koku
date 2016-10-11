package com.yuqirong.koku.app;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016-9-29.
 */
public class ApiClient {

    private static volatile ApiClient sApiClient;

    private Gson mGson;

    private final WeiboApi mWeiboApi;

    private ApiClient() {
        mGson = new Gson();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstant.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson)).build();
        mWeiboApi = retrofit.create(WeiboApi.class);
        mGson = new Gson();
    }

    public static ApiClient getInstance() {
        if (sApiClient == null) {
            synchronized (ApiClient.class) {
                if (sApiClient == null) {
                    sApiClient = new ApiClient();
                }
            }
        }
        return sApiClient;
    }

    public WeiboApi getWeiboApi(){
        return mWeiboApi;
    }

    public Gson getGson(){
        return mGson;
    }

}
