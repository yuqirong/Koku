package com.yuqirong.koku.module.model.interactor.impl;

import com.yuqirong.koku.app.AppService;
import com.yuqirong.koku.module.model.entity.OAuthInfo;
import com.yuqirong.koku.module.model.interactor.IAuthorizeInteractor;

import java.util.Map;

import rx.Observable;

/**
 * Created by Administrator on 2016-9-30.
 */

public class IAuthorizeInteractorImpl implements IAuthorizeInteractor {
    @Override
    public Observable<OAuthInfo> getOAuthInfo(Map<String, String> filedMap) {
        return AppService.getInstance().getApiClient().getWeiboApi().getOAuthInfo(filedMap);
    }
}
