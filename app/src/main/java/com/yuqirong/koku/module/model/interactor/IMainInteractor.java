package com.yuqirong.koku.module.model.interactor;

import com.yuqirong.koku.module.model.entity.AccessTokenInfo;

import rx.Observable;

/**
 * Created by Administrator on 2016-9-29.
 */
public interface IMainInteractor {

    Observable<AccessTokenInfo> checkTokenExpireIn(String accessToken);

}
