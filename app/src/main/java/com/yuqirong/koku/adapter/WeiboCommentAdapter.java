package com.yuqirong.koku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.Comment;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.StringUtils;

/**
 * 评论Adapter
 * Created by Anyway on 2015/10/11.
 */
public class WeiboCommentAdapter extends LoadMoreAdapter<Comment> {

    private Context context;
    private static ImageLoader imageLoader;
    private static DisplayImageOptions options;

    public WeiboCommentAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        options = BitmapUtil.getDisplayImageOptions(R.drawable.img_empty_avatar, true, false);
    }

    /**
     * 设置成 “没有更多评论了”
     */
    public void setLoadFinish() {
        completeLoadMore(false);
        loadSuccess = true;
        setIsLoadingMore(true);
        if (mFooterViewHolder != null) {
            mFooterViewHolder.tv_load_fail.setText(context.getResources().getString(R.string.load_finish));
        }
    }

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
        imageLoader.displayImage(comment.user.profile_image_url, mViewHolder.iv_avatar, options);
        mViewHolder.tv_screen_name.setText(comment.user.screen_name);
        mViewHolder.tv_time.setText(DateUtils.getWeiboDate(comment.created_at));
        mViewHolder.tv_device.setText(Html.fromHtml(comment.source));
        SpannableString weiBoContent = StringUtils.getWeiBoContent(mViewHolder.context, comment.text, mViewHolder.tv_text);
        mViewHolder.tv_text.setText(weiBoContent);
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
