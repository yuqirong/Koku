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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.adapter.DraftRecyclerViewAdapter;
import com.yuqirong.koku.db.DraftDB;
import com.yuqirong.koku.entity.Draft;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;

import java.util.List;

/**
 * 草稿箱Fragment
 * Created by Anyway on 2015/10/6.
 */
public class DraftFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    protected static DraftRecyclerViewAdapter adapter;
    private static final int START_PUBLISH_ACTIVITY = 1300;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private Draft draft;

    @Override
    public void initData(Bundle savedInstanceState) {
        getDrafts();
    }

    public void getDrafts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter.getList().clear();
                List<Draft> list = DraftDB.getDraftList();
                if (list == null || list.size() == 0) {
                    return;
                }
                adapter.getList().addAll(list);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
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

    private void processPublish(int position) {
        Intent intent = new Intent(context, PublishActivity.class);
        draft = adapter.getList().get(position);
        intent.putExtra("type", draft.type);
        intent.putExtra("text", draft.text);
        intent.putExtra("draft_id", draft.id);
        intent.putExtra("idstr", draft.idstr);
        // TODO: 2015/10/12
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
                    new DraftDBTask().execute(draft.id);
                    getDrafts();
                    CommonUtil.setVubator(context, 300);
                }
            }
        }
    }

    private void processDeleteDraft(int id, int position) {
        adapter.getList().remove(position);
        new DraftDBTask().execute(id);
    }

    static class DraftDBTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... param) {
            DraftDB.deleteDraft(param[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }

}
