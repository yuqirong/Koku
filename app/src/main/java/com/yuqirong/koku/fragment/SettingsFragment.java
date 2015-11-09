package com.yuqirong.koku.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CompoundButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.AuthorizeActivity;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.AboutTextView;
import com.yuqirong.koku.view.SettingsView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/4.
 */
public class SettingsFragment extends BaseFragment {

    private SettingsView sv_browser;
    private SettingsView sv_vibrator;

    @Override
    public void initData(Bundle savedInstanceState) {
        sv_browser.setChecked(SharePrefUtil.getBoolean(context, "built-in_browser", true));
        sv_vibrator.setChecked(SharePrefUtil.getBoolean(context, "vibrate_feedback", true));
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        AboutTextView atv_logout = (AboutTextView) view.findViewById(R.id.atv_logout);
        AboutTextView atv_feedback = (AboutTextView) view.findViewById(R.id.atv_feedback);
        sv_browser = (SettingsView) view.findViewById(R.id.sv_browser);
        sv_vibrator = (SettingsView) view.findViewById(R.id.sv_vibrator);
        AboutTextView atv_font_size = (AboutTextView) view.findViewById(R.id.atv_font_size);

        sv_browser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePrefUtil.saveBoolean(context, "built-in_browser", isChecked);
            }
        });

        sv_vibrator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePrefUtil.saveBoolean(context, "vibrate_feedback", isChecked);
            }
        });
        atv_font_size.setOnClickListener(listener);
        atv_feedback.setOnClickListener(listener);
        atv_logout.setOnClickListener(listener);
        return view;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.atv_logout:
                    logout();
                    break;
                case R.id.atv_feedback:
                    feedback();
                    break;
                case R.id.atv_font_size:
                    //可以通过getResources().updateConfiguration修改fontScale值，
                    //会影响全部使用sp作单位的缩放系数
                    CommonUtil.createItemAlertDialog(context, new CharSequence[]{"小号字体", "标准字体", "大号字体"},
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    break;
            }
        }

    };

    //意见反馈
    private void feedback() {
        Intent intent = new Intent();
        intent.setClass(context, PublishActivity.class);
        intent.putExtra("type", PublishActivity.SEND_WEIBO);
        StringBuffer buffer = new StringBuffer();
        buffer.append(context.getString(R.string.feedback_text));
        buffer.append(" " + Build.MODEL + ";");
        buffer.append(Build.VERSION.RELEASE + ";");
        String version = CommonUtil.getVersionName(context);
        if (!TextUtils.isEmpty(version)) {
            buffer.append(version + ";");
        }
        intent.putExtra("text", buffer.toString());
        context.startActivity(intent);
    }

    //注销账号，取消授权
    private void logout() {
        CommonUtil.createMessageAlertDialog(context, context.getString(R.string.tip),
                context.getString(R.string.confirm_cacel_auth), context.getString(R.string.cancel),
                null, context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = AppConstant.OAUTH2_REVOKEOAUTH2 + "?access_token=" +
                                SharePrefUtil.getString(context, "access_token", "");
                        LogUtils.i("取消授权的url ：" + url);
                        getJsonData(url, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    String result = jsonObject.getString("result");
                                    if ("true".equals(result)) {
                                        SharePrefUtil.saveString(context, "access_token", null);
                                        SharePrefUtil.saveString(context, "screen_name", null);
                                        SharePrefUtil.saveString(context, "expires_in", null);
                                        SharePrefUtil.saveString(context, "avatar_large", null);
                                        SharePrefUtil.saveString(context, "expire_in", null);
                                        SharePrefUtil.saveString(context, "remind_in", null);
                                        SharePrefUtil.saveString(context, "uid", null);
                                        AuthorizeActivity.actionStart(context);
                                    } else {
                                        CommonUtil.showToast(context, context.getString(R.string.cancel_auth_fail));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                LogUtils.i("取消授权 ：network error");
                            }
                        });
                    }
                }, true);
    }

}
