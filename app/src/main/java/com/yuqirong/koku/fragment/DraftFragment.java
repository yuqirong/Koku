package com.yuqirong.koku.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.adapter.DraftRecyclerViewAdapter;
import com.yuqirong.koku.application.MyApplication;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.db.DraftDB;
import com.yuqirong.koku.entity.Draft;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 草稿箱Fragment
 * Created by Anyway on 2015/10/6.
 */
public class DraftFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    protected static DraftRecyclerViewAdapter adapter;
    private static final int START_PUBLISH_ACTIVITY = 1300;

    private static Handler handler = new Handler();
    private Draft draft;

    @Override
    public void initData(Bundle savedInstanceState) {
        getDrafts();
    }

    public void getDrafts() {
        MyApplication.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                adapter.getList().clear();
                adapter.getList().addAll(DraftDB.getDraftList());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draft, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DraftRecyclerViewAdapter();
        adapter.setOnItemClickListener(new DraftRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position, final int id) {
                switch (view.getId()) {
                    case R.id.ib_delete:
                        CommonUtil.createMessageAlertDialog(context, getResources().getString(R.string.tip),
                                getResources().getString(R.string.delete_draft), getResources().getString(R.string.cancel),
                                null, getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        processDeleteDraft(id, position);
                                    }
                                }, true);
                        break;
                    case R.id.ib_send:
                        processSend(position, id);
                        break;
                    default:
                        LogUtils.i("click the draft id = " + id + ", position = " + position);
                        processPublish(position);
                        break;
                }
            }

        });
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    private void processSend(final int position, final int id) {
        String url = null;
        final Draft draft = adapter.getList().get(position);
        switch (draft.type) {
            case PublishActivity.SEND_WEIBO:   //发微博
                url = AppConstant.STATUSES_UPDATE_URL;
                break;
            case PublishActivity.SEND_COMMENT:  //评论
                url = AppConstant.COMMENTS_CREATE_URL;
                break;
            case PublishActivity.SEND_REPOST:  //转发
                url = AppConstant.STATUSES_REPOST_URL;
                break;
            default:
                break;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String idstr = jsonObject.getString("idstr");
                    if (!TextUtils.isEmpty(idstr)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DraftDB.deleteDraft(id);
                            }
                        }).start();
                        adapter.getList().remove(position);
                        adapter.notifyDataSetChanged();
                        CommonUtil.setVubator(context,300);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("access_token", SharePrefUtil.getString(context, "access_token", ""));
                switch (draft.type) {
                    case PublishActivity.SEND_WEIBO:
                        map.put("status", draft.text);
                        break;
                    case PublishActivity.SEND_COMMENT:
                        map.put("comment", draft.text);
                        map.put("id", draft.idstr);
                        break;
                    case PublishActivity.SEND_REPOST:
                        map.put("status", draft.text);
                        map.put("id", draft.idstr);
                        break;
                }
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            CommonUtil.showToast(context, "Network Error");
        }
    };

    private void processPublish(int position) {
        Intent intent = new Intent(context, PublishActivity.class);
        draft = adapter.getList().get(position);
        intent.putExtra("type", draft.type);
        intent.putExtra("text", draft.text);
        intent.putExtra("draft_id", draft.id);
        intent.putExtra("idstr", draft.idstr);
        startActivityForResult(intent, START_PUBLISH_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_PUBLISH_ACTIVITY) {
            if (resultCode == PublishActivity.REFRESH_DRAFT) {
                getDrafts();
            } else if (resultCode == PublishActivity.SEND_WEIBO_SUCCESS
                    || resultCode == PublishActivity.SEND_COMMENT_SUCCESS
                    || resultCode == PublishActivity.SEND_REPOST_SUCCESS) {
                if (draft != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DraftDB.deleteDraft(draft.id);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    getDrafts();
                                }
                            });
                        }
                    }).start();
                    CommonUtil.setVubator(context, 300);
                }
            }
        }
    }

    private void processDeleteDraft(final int id, int position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DraftDB.deleteDraft(id);
            }
        }).start();
        adapter.getList().remove(position);
        adapter.notifyDataSetChanged();
    }

}
