package com.yuqirong.koku.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.RemindComment;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import org.w3c.dom.Text;

/**
 * 提醒评论的adpter
 * Created by Administrator on 2015/11/16.
 */
public class CommentRemindAdapter extends LoadMoreAdapter<RemindComment> {

    private Context context;

    @Override
    public RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.weibo_comment, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void bindCustomViewHolder(RecyclerView.ViewHolder holder, int position) {
        RemindComment remindComment = list.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        //设置名字
        if (SharePrefUtil.getBoolean(context, "user_remark", true)) {
            viewHolder.tv_screen_name.setText(remindComment.getUser().getName());
        } else {
            viewHolder.tv_screen_name.setText(remindComment.getUser().getScreen_name());
        }
        //设置评论时间
        viewHolder.tv_time.setText(DateUtils.getWeiboDate(remindComment.getCreated_at()));
        //设置来源
        viewHolder.tv_device.setText(Html.fromHtml(remindComment.getSource()));
        //设置认证图标
        switch (remindComment.getUser().getVerified_type()) {
            case 0:
                viewHolder.iv_verified.setImageResource(R.drawable.avatar_vip);
                break;
            case -1:
                viewHolder.iv_verified.setImageResource(android.R.color.transparent);
                break;
            case 220:
                viewHolder.iv_verified.setImageResource(R.drawable.avatar_grassroot);
                break;
            default:
                viewHolder.iv_verified.setImageResource(R.drawable.avatar_enterprise_vip);
                break;
        }
        viewHolder.tv_text.setText(remindComment.getText());
        if (remindComment.getReply_comment() != null) {
            viewHolder.tv_reply_text.setText(remindComment.getReply_comment().getText());
        }
        viewHolder.iv_weibo_name.setText(remindComment.getStatus().user.getScreen_name());
        viewHolder.iv_weibo_text.setText(remindComment.getStatus().text);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_screen_name;
        public ImageView iv_verified;
        public TextView tv_time;
        public TextView tv_device;
        public ImageView iv_comment;
        public TextView tv_text;
        public TextView tv_reply_text;
        public ImageView iv_weibo_avatar;
        public TextView iv_weibo_name;
        public TextView iv_weibo_text;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            iv_verified = (ImageView) itemView.findViewById(R.id.iv_verified);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_device = (TextView) itemView.findViewById(R.id.tv_device);
            iv_comment = (ImageView) itemView.findViewById(R.id.iv_comment);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_reply_text = (TextView) itemView.findViewById(R.id.tv_reply_text);
            iv_weibo_avatar = (ImageView) itemView.findViewById(R.id.iv_weibo_avatar);
            iv_weibo_name = (TextView) itemView.findViewById(R.id.iv_weibo_name);
            iv_weibo_text = (TextView) itemView.findViewById(R.id.iv_weibo_text);
        }
    }

}
