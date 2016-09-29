package com.yuqirong.koku.module.presenter;

import com.yuqirong.koku.module.model.entity.AccessTokenInfo;
import com.yuqirong.koku.module.model.interactor.IMainInteractor;
import com.yuqirong.koku.module.model.interactor.impl.IMainInteractorImpl;
import com.yuqirong.koku.module.view.IMainView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-9-29.
 */

public class MainPresenter extends BasePresenter<IMainView> {

    private IMainInteractor mIMainInteractor;

    public MainPresenter() {
        mIMainInteractor = new IMainInteractorImpl();
    }

    public void checkTokenExpireIn(String accessToken) {
        mIMainInteractor.checkTokenExpireIn(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AccessTokenInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AccessTokenInfo tokenInfo) {
                        String expireIn = tokenInfo.getExpireIn();
                        int expireTime;
                        try {
                            expireTime = Integer.parseInt(expireIn);
                            if (expireTime > 0) {
                                mView.updateTokenInfo(tokenInfo);
                            } else {
                                mView.goToAuthorizeActivity();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            mView.goToAuthorizeActivity();
                        }
                    }
                });
    }

}
