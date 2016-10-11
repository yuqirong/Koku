package com.yuqirong.koku.module.presenter;

import com.yuqirong.koku.app.MyApplication;
import com.yuqirong.koku.module.model.entity.OAuthInfo;
import com.yuqirong.koku.module.model.interactor.IAuthorizeInteractor;
import com.yuqirong.koku.module.model.interactor.impl.IAuthorizeInteractorImpl;
import com.yuqirong.koku.module.view.IAuthorizeView;
import com.yuqirong.koku.util.SharePrefUtil;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-9-30.
 */

public class AuthorizePresenter extends BasePresenter<IAuthorizeView> {

    private IAuthorizeInteractor mIAuthorizeInteractor;

    public AuthorizePresenter(){
        mIAuthorizeInteractor = new IAuthorizeInteractorImpl();
    }

    public void getOAuthInfo(Map<String,String> filedMap){
        mIAuthorizeInteractor.getOAuthInfo(filedMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OAuthInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OAuthInfo oAuthInfo) {
                        if (oAuthInfo.getAccessToken() != null) {
                            SharePrefUtil.saveString(MyApplication.getsContext(), "access_token", oAuthInfo.getAccessToken());
                        }
                        if (oAuthInfo.getExpiresIn() != null) {
                            SharePrefUtil.saveString(MyApplication.getsContext(), "expires_in", oAuthInfo.getExpiresIn());
                        }
                        if (oAuthInfo.getRemindIn() != null) {
                            SharePrefUtil.saveString(MyApplication.getsContext(), "remind_in", oAuthInfo.getRemindIn());
                        }
                        if (oAuthInfo.getUid() != null) {
                            SharePrefUtil.saveString(MyApplication.getsContext(), "uid", oAuthInfo.getUid());
                        }
                        mView.goToMainActivity();
                    }
                });

    }

}
