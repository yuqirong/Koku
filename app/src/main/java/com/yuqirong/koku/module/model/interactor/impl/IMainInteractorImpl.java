package com.yuqirong.koku.module.model.interactor.impl;

import com.yuqirong.koku.app.AppService;
import com.yuqirong.koku.module.model.entity.AccessTokenInfo;
import com.yuqirong.koku.module.model.interactor.IMainInteractor;

import rx.Observable;

/**
 * Created by Administrator on 2016-9-29.
 */

public class IMainInteractorImpl implements IMainInteractor {

    @Override
    public Observable<AccessTokenInfo> checkTokenExpireIn(String accessToken) {
        return AppService.getInstance().getApiClient().getWeiboApi().getAccessTokenInfo(accessToken);
    }

}
