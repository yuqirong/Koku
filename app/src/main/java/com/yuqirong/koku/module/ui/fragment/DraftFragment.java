package com.yuqirong.koku.module.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.module.ui.activity.PublishActivity;
import com.yuqirong.koku.module.ui.adapter.DraftRecyclerViewAdapter;
import com.yuqirong.koku.app.MyApplication;
import com.yuqirong.koku.app.AppConstant;
import com.yuqirong.koku.db.DraftDB;
import com.yuqirong.koku.module.model.entity.Draft;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 草稿箱Fragment
 * Created by Anyway on 2015/10/6.
 */
public class DraftFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private DraftRecyclerViewAdapter adapter;
    private static final int START_PUBLISH_ACTIVITY = 1300;

    private static Handler handler = new Handler();
    private Draft draft;
    private TextView tv_no_draft;

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
                        if (adapter.getList().size() == 0) {
                            tv_no_draft.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        } else {
                            mRecyclerView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draft, null);
        tv_no_draft = (TextView) view.findViewById(R.id.tv_no_draft);
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
                        MyApplication.getExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                DraftDB.deleteDraft(id);
                            }
                        });
                        adapter.getList().remove(position);
                        adapter.notifyDataSetChanged();
                        CommonUtil.showNotification(context, R.string.send_remind, R.string.send_success, R.drawable.ic_done_light, true);
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
                    MyApplication.getExecutor().execute(new Runnable() {
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
                    });
                    CommonUtil.showNotification(context, R.string.send_remind, R.string.send_success, R.drawable.ic_done_light, true);
                }
            }
        }
    }

    private void processDeleteDraft(final int id, int position) {
        MyApplication.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                DraftDB.deleteDraft(id);
            }
        });
        adapter.getList().remove(position);
        adapter.notifyDataSetChanged();
    }

}
