package com.yuqirong.koku.module.view;

import com.yuqirong.koku.module.model.entity.AccessTokenInfo;

/**
 * Created by Administrator on 2016-9-29.
 */

public interface IMainView extends BaseView {

    void goToAuthorizeActivity();

    void updateTokenInfo(AccessTokenInfo accessTokenInfo);

}
