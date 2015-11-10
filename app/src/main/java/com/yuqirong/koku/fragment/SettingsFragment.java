package com.yuqirong.koku.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.AuthorizeActivity;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.receiver.RefreshWeiboTimelineReceiver;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.AboutTextView;
import com.yuqirong.koku.view.DoubleTextSettingsView;
import com.yuqirong.koku.view.SettingsView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/4.
 */
public class SettingsFragment extends BaseFragment {

    private SettingsView sv_browser;
    private DoubleTextSettingsView dsv_vibrator;
    private DoubleTextSettingsView dsv_refresh;
    private SettingsView sv_remark;
    private String[] fontSizeArray;
    private String[] fabStringArray;
    private String[] fabPositionStringArray;
    private AboutTextView atv_font_size;
    private AboutTextView atv_fab_function;
    private AboutTextView atv_fab_position;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fontSizeArray = context.getResources().getStringArray(R.array.font_size);
        fabStringArray = context.getResources().getStringArray(R.array.fab_function);
        fabPositionStringArray = context.getResources().getStringArray(R.array.fab_position);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        //是否内置浏览器
        sv_browser.setChecked(SharePrefUtil.getBoolean(context, "built-in_browser", true));
        //是否振动反馈
        dsv_vibrator.setChecked(SharePrefUtil.getBoolean(context, "vibrate_feedback", true));
        //用户名备注
        sv_remark.setChecked(SharePrefUtil.getBoolean(context, "user_remark", true));
        //字体大小
        int selectedSize = SharePrefUtil.getInt(context, "font_size", 1);
        atv_font_size.setContent(fontSizeArray[selectedSize].toString());
        //列表自动刷新
        dsv_refresh.setChecked(SharePrefUtil.getBoolean(context, "timeline_refresh", true));
        //fab功能
        int fabFunction = SharePrefUtil.getInt(context, "fab_function", 0);
        atv_fab_function.setContent(fabStringArray[fabFunction].toString());
        //fab位置
        int fabPosition = SharePrefUtil.getInt(context, "fab_position", 0);
        atv_fab_position.setContent(fabPositionStringArray[fabPosition].toString());
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        AboutTextView atv_logout = (AboutTextView) view.findViewById(R.id.atv_logout);
        AboutTextView atv_feedback = (AboutTextView) view.findViewById(R.id.atv_feedback);
        atv_fab_position = (AboutTextView) view.findViewById(R.id.atv_fab_position);
        atv_fab_function = (AboutTextView) view.findViewById(R.id.atv_fab_function);
        sv_browser = (SettingsView) view.findViewById(R.id.sv_browser);
        dsv_refresh = (DoubleTextSettingsView) view.findViewById(R.id.dsv_refresh);
        dsv_vibrator = (DoubleTextSettingsView) view.findViewById(R.id.dsv_vibrator);
        atv_font_size = (AboutTextView) view.findViewById(R.id.atv_font_size);
        sv_remark = (SettingsView) view.findViewById(R.id.sv_remark);
        sv_remark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePrefUtil.saveBoolean(context, "user_remark", isChecked);
            }
        });
        sv_browser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePrefUtil.saveBoolean(context, "built-in_browser", isChecked);
            }
        });
        dsv_vibrator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePrefUtil.saveBoolean(context, "vibrate_feedback", isChecked);
            }
        });
        dsv_refresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharePrefUtil.saveBoolean(context, "timeline_refresh", isChecked);
            }
        });
        atv_fab_position.setOnClickListener(listener);
        atv_fab_function.setOnClickListener(listener);
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
                    selectFontSize();
                    break;
                case R.id.atv_fab_function:
                    selectFabFunction();
                    break;
                case R.id.atv_fab_position:
                    selectFabPosition();
            }
        }

    };

    private void selectFabPosition() {
        final int fabPosition = SharePrefUtil.getInt(context, "fab_position", 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.fab_position).setSingleChoiceItems(R.array.fab_position, fabPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharePrefUtil.saveInt(context, "fab_position", which);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharePrefUtil.saveInt(context, "fab_position", fabPosition);
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int fabPosition = SharePrefUtil.getInt(context, "fab_position", 0);
                atv_fab_position.setContent(fabPositionStringArray[fabPosition]);
                Intent intent = new Intent();
                intent.putExtra("flag", 1);
                intent.setAction(RefreshWeiboTimelineReceiver.INTENT_FILTER_NAME);
                context.sendBroadcast(intent);
            }
        }).setCancelable(true).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SharePrefUtil.saveInt(context, "fab_position", fabPosition);
            }
        }).create().show();
    }

    //选择fab功能
    private void selectFabFunction() {
        final int fabFunction = SharePrefUtil.getInt(context, "fab_function", 0);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.fab_function).setSingleChoiceItems(R.array.fab_function, fabFunction, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharePrefUtil.saveInt(context, "fab_function", which);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharePrefUtil.saveInt(context, "fab_function", fabFunction);
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int fabFunction = SharePrefUtil.getInt(context, "fab_function", 0);
                atv_fab_function.setContent(fabStringArray[fabFunction]);
                Intent intent = new Intent();
                intent.putExtra("flag", 1);
                intent.setAction(RefreshWeiboTimelineReceiver.INTENT_FILTER_NAME);
                context.sendBroadcast(intent);
            }
        }).setCancelable(true).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SharePrefUtil.saveInt(context, "fab_function", fabFunction);
            }
        }).create().show();
    }

    //选择字体大小
    private void selectFontSize() {
        //可以通过getResources().updateConfiguration修改fontScale值，
        //会影响全部使用sp作单位的缩放系数
        final int selectedSize = SharePrefUtil.getInt(context, "font_size", 1);
        final Configuration configuration = new Configuration();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.font_size).setSingleChoiceItems(R.array.font_size, selectedSize,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                configuration.fontScale = 0.8f;
                                break;
                            case 1:
                                configuration.fontScale = 1.0f;
                                break;
                            case 2:
                                configuration.fontScale = 1.2f;
                                break;
                        }
                        SharePrefUtil.saveInt(context, "font_size", which);
                    }
                });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharePrefUtil.saveInt(context, "font_size", selectedSize);
            }
        }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.getResources().updateConfiguration(configuration, null);
                int selectedSize = SharePrefUtil.getInt(context, "font_size", 1);
                atv_font_size.setContent(fontSizeArray[selectedSize].toString());
            }
        });
        builder.setCancelable(true).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SharePrefUtil.saveInt(context, "font_size", selectedSize);
            }
        }).create().show();
    }

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
