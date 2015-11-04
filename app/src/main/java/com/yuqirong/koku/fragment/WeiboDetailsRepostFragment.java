package com.yuqirong.koku.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.LoadMoreAdapter;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.Comment;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.JsonUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.util.StringUtils;
import com.yuqirong.koku.view.AutoLoadRecyclerView;
import com.yuqirong.koku.view.DividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anyway on 2015/10/11.
 */
public class WeiboDetailsRepostFragment extends BaseFragment {

    private View headerView;
    private AutoLoadRecyclerView mAutoLoadRecyclerView;
    private RepostAdapter adapter;

    public WeiboDetailsRepostFragment(View headerView) {
        super();
        this.headerView = headerView;
    }

    public WeiboDetailsRepostFragment() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        String idstr = getArguments().getString("idstr");
        if (!TextUtils.isEmpty(idstr)) {
            String url = AppConstant.STATUSES_REPOST_TIMELINE_URL + "?count=20&id=" + idstr + "&access_token=" + SharePrefUtil.getString(context, "access_token", "");
            LogUtils.i("转发url ：" + url);
            getJsonData(url, listener, errorListener);
        }
    }

    Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject object) {
            try {
                String str = object.getString("reposts");
                adapter.getList().addAll(adapter.getList().size() - 1, JsonUtils.getListFromJson(str, Comment.class));
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAutoLoadRecyclerView = new AutoLoadRecyclerView(context);
        mAutoLoadRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        adapter = new RepostAdapter();
        if (headerView != null) {
            ViewParent parent = headerView.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(headerView);
            }
            adapter.addHeaderView(true, headerView);
        }
        adapter.getList().add(new Comment());
        adapter.getList().add(new Comment());
        mAutoLoadRecyclerView.setAdapter(adapter);
        return mAutoLoadRecyclerView;
    }

    static class RepostAdapter extends LoadMoreAdapter<Comment> {

        @Override
        public RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weibo_comment_item, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void bindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
            LogUtils.i("position : " + position);
            Comment comment = getList().get(position);
            ViewHolder mViewHolder = (ViewHolder) holder;
            mViewHolder.tv_screen_name.setText(comment.user.screen_name);
            mViewHolder.tv_time.setText(DateUtils.getWeiboDate(comment.created_at));
            mViewHolder.tv_device.setText(Html.fromHtml(comment.source));
            SpannableString weiBoContent = StringUtils.getWeiBoContent(mViewHolder.context, comment.text, mViewHolder.tv_text);
            mViewHolder.tv_text.setText(weiBoContent);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_screen_name;
        public TextView tv_time;
        public TextView tv_device;
        public TextView tv_text;
        public Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_device = (TextView) itemView.findViewById(R.id.tv_device);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
        }

    }
}
