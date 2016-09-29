package com.yuqirong.koku.module.presenter;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016-9-29.
 */
public abstract class BasePresenter<T> {

    protected T mView;
    protected CompositeSubscription mCompositeSubscription;

    public void attachView(T tView) {
        mView = tView;
        mCompositeSubscription = new CompositeSubscription();
    }

    public void detachView() {
        if (mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

}
