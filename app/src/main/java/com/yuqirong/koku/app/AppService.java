package com.yuqirong.koku.app;

/**
 * Created by Administrator on 2016-9-29.
 */

public class AppService {

    private static volatile AppService sAppService;

    private final ApiClient mApiClient;

    private AppService() {
        mApiClient = ApiClient.getInstance();
    }

    public static AppService getInstance() {
        if (sAppService == null) {
            synchronized (AppService.class) {
                if (sAppService == null) {
                    sAppService = new AppService();
                }
            }
        }
        return sAppService;
    }

    public ApiClient getApiClient(){
        return mApiClient;
    }

}
