package com.yuqirong.koku.module.model.interactor;

import com.yuqirong.koku.module.model.entity.OAuthInfo;

import java.util.Map;

import rx.Observable;

/**
 * Created by Administrator on 2016-9-30.
 */

public interface IAuthorizeInteractor {

    Observable<OAuthInfo> getOAuthInfo(Map<String,String> filedMap);

}
