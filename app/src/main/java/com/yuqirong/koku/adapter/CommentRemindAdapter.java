package com.yuqirong.koku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.MainActivity;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.entity.RemindComment;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.util.StringUtils;

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
        final RemindComment remindComment = list.get(position);
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
        //评论
        SpannableString text = StringUtils.getWeiBoContent(context,
                remindComment.getText(), viewHolder.tv_text);
        viewHolder.tv_text.setText(text);
        //被回复的评论
        if (remindComment.getReply_comment() != null) {
            if (!TextUtils.isEmpty(remindComment.getReply_comment().getText())) {
                viewHolder.tv_reply_text.setVisibility(View.VISIBLE);
                SpannableString replyContent = StringUtils.getWeiBoContent(context,
                        remindComment.getReply_comment().getText(), viewHolder.tv_reply_text);
                viewHolder.tv_reply_text.setText(replyContent);
            }
        } else {
            viewHolder.tv_reply_text.setVisibility(View.GONE);
        }
        //微博的原博主
        viewHolder.iv_weibo_name.setText(remindComment.getStatus().getUser().getScreen_name());
        //微博的内容
        SpannableString weiBoContent = StringUtils.getWeiBoContent(context,
                remindComment.getStatus().getText(), viewHolder.iv_weibo_text);
        viewHolder.iv_weibo_text.setText(weiBoContent);

        Glide.with(context).load(remindComment.getUser().getProfile_image_url())
                .centerCrop()
                .placeholder(R.drawable.img_empty_avatar)
                .crossFade()
                .into(viewHolder.iv_avatar);

        Glide.with(context).load(remindComment.getStatus().getUser().getAvatar_large())
                .centerCrop()
                .placeholder(R.drawable.img_empty_avatar)
                .crossFade()
                .into(viewHolder.iv_weibo_avatar);

        viewHolder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), PublishActivity.class);
                intent.putExtra("type", PublishActivity.REPLY_COMMENT);
                intent.putExtra("cid", remindComment.getIdstr());
                intent.putExtra("idstr", remindComment.getStatus().getIdstr());
                context.startActivity(intent);
            }
        });

    }

//    View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.tv_comment:
//                    Intent intent = new Intent();
//                    intent.setClass(context, PublishActivity.class);
//                    intent.putExtra("type", PublishActivity.SEND_COMMENT);
//                    intent.putExtra("idstr", .idstr);
//                    context.startActivity(intent);
//                    break;
//                break;
//            }
//        }
//    };

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_screen_name;
        public ImageView iv_verified;
        public TextView tv_time;
        public TextView tv_device;
        public TextView tv_comment;
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
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_reply_text = (TextView) itemView.findViewById(R.id.tv_reply_text);
            iv_weibo_avatar = (ImageView) itemView.findViewById(R.id.iv_weibo_avatar);
            iv_weibo_name = (TextView) itemView.findViewById(R.id.iv_weibo_name);
            iv_weibo_text = (TextView) itemView.findViewById(R.id.iv_weibo_text);
        }
    }

}
