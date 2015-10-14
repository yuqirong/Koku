package com.yuqirong.koku.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.entity.Comment;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.CommonUtil;
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
    private static WeiboRecycleViewAdapter.OnItemClickListener listener;
    public static final int REPLY_COMMENT = 1007;
    private String weiboId;

   public void setOnItemClickListener(WeiboRecycleViewAdapter.OnItemClickListener listener){
       this.listener = listener;
   }

    public WeiboCommentAdapter(Context context,String weiboId) {
        this.context = context;
        this.weiboId = weiboId;
        imageLoader = ImageLoader.getInstance();
        options = BitmapUtil.getDisplayImageOptions(R.drawable.img_empty_avatar, true, false);
    }

    /**
     * 设置成 “没有更多评论了”
     */
    public void setLoadFinish() {
        completeLoadMore(false);
        loadSuccess = true;
        if (mFooterViewHolder != null) {
            mFooterViewHolder.tv_load_fail.setText(context.getResources().getString(R.string.load_finish));
        }
    }

    public void initFooterViewHolder() {
        if (mFooterViewHolder != null) {
            mFooterViewHolder.tv_load_fail.setVisibility(View.INVISIBLE);
            mFooterViewHolder.ll_load_more.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weibo_comment_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LogUtils.i("position : " + position);
        Comment comment = getList().get(position);
        ViewHolder mViewHolder = (ViewHolder) holder;
        imageLoader.displayImage(comment.user.profile_image_url, mViewHolder.iv_avatar, options);
        mViewHolder.tv_screen_name.setText(comment.user.screen_name);
        mViewHolder.tv_time.setText(DateUtils.getWeiboDate(comment.created_at));
        mViewHolder.tv_device.setText(Html.fromHtml(comment.source));
        SpannableString weiBoContent = StringUtils.getWeiBoContent(mViewHolder.context, comment.text, mViewHolder.tv_text);
        mViewHolder.tv_text.setText(weiBoContent);
        mViewHolder.iv_overflow.setOnClickListener(new ViewOnClickListener(comment,weiboId));
        mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onItemClick(v,position);
                }
            }
        });
    }

     static class ViewOnClickListener implements View.OnClickListener{

         private Comment comment;
         private String weiboId;

         public ViewOnClickListener(Comment comment,String weiboId){
             this.comment = comment;
             this.weiboId = weiboId;
         }

         @Override
         public void onClick(final View v) {
             // TODO: 2015/10/14
             CommonUtil.showPopupMenu(v.getContext(), v, R.menu.overflow_comment_popupmenu, new PopupMenu.OnMenuItemClickListener() {
                 @Override
                 public boolean onMenuItemClick(MenuItem item) {
                     switch (item.getItemId()){
                         case R.id.overflow_comment:
//                            int requestCode = MainActivity.SEND_NEW_REPOST;
                             Intent intent = new Intent();
                             intent.setClass(v.getContext(), PublishActivity.class);
                             intent.putExtra("type", PublishActivity.REPLY_COMMENT);
                             intent.putExtra("cid", comment.idstr);
                             intent.putExtra("idstr", weiboId);
                             v.getContext().startActivity(intent);
                             break;
                         case R.id.overflow_copy:

                             break;
                     }
                     return false;
                 }
             });
         }
     }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_avatar;
        public TextView tv_screen_name;
        public TextView tv_time;
        public TextView tv_device;
        public TextView tv_text;
        public Context context;
        public ImageView iv_overflow;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_device = (TextView) itemView.findViewById(R.id.tv_device);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            iv_overflow = (ImageView) itemView.findViewById(R.id.iv_overflow);
        }

    }
}
