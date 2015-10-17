package com.yuqirong.koku.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuqirong.koku.R;
import com.yuqirong.koku.activity.MainActivity;
import com.yuqirong.koku.activity.MyFavoriteActivity;
import com.yuqirong.koku.activity.NearlyDynamicActivity;
import com.yuqirong.koku.activity.PublishActivity;
import com.yuqirong.koku.activity.UserDetailsActivity;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.entity.Pic_urls;
import com.yuqirong.koku.entity.Status;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 微博适配器
 * Created by Anyway on 2015/8/30.
 */
public class WeiboRecycleViewAdapter extends LoadMoreAdapter<Status> {

    private Context context;
    private ImageLoader imageLoader;
    private static DisplayImageOptions options;
    private static DisplayImageOptions ninepic_options;

    public static final String AT = "@";
    private static final int[] IMAGEVIEW_IDS = new int[]{R.id.iv_01, R.id.iv_02, R.id.iv_03, R.id.iv_04, R.id.iv_05, R.id.iv_06, R.id.iv_07, R.id.iv_08, R.id.iv_09};
    private Status status;
    private RequestQueue mQueue;
    private View view;

    public WeiboRecycleViewAdapter(Context context) {
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        options = BitmapUtil.getDisplayImageOptions(R.drawable.img_empty_avatar, true, true);
        ninepic_options = BitmapUtil.getDisplayImageOptions(R.drawable.thumbnail_default, true, true);
        list.add(new Status());
        mQueue = Volley.newRequestQueue(context);
    }

    @Override
    public RecyclerView.ViewHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        view = parent;
        View view = LayoutInflater.from(context).inflate(R.layout.weibo_original_item, parent, false);
        WeiboViewHolder holder = new WeiboViewHolder(view);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(RecyclerView.ViewHolder holder, final int position) {
        status = list.get(position);
        WeiboViewHolder viewHolder = (WeiboViewHolder) holder;
        //创建监听器实例
        viewHolder.onMenuItemClickListener = new MyOnMenuItemClickListener(position);
        viewHolder.onClickListener = new WeiboWidghtOnClickListener(position, viewHolder.onMenuItemClickListener);

        viewHolder.tv_screen_name.setText(status.user.name);
        imageLoader.displayImage(status.user.profile_image_url, viewHolder.iv_avatar, options);
        viewHolder.tv_time.setText(DateUtils.getWeiboDate(status.created_at));
        viewHolder.tv_device.setText(Html.fromHtml(status.source));
        //设置认证图标
        switch (status.user.verified_type) {
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
        //设置微博 转发数和评论数
        viewHolder.tv_repost_count.setText(CommonUtil.getNumString(status.reposts_count));
        viewHolder.tv_comment_count.setText(CommonUtil.getNumString(status.comments_count));
        //设置微博内容
        SpannableString weiBoContent = StringUtils.getWeiBoContent(context, status.text, viewHolder.tv_text);
        viewHolder.tv_text.setText(weiBoContent);
        //判断此微博是否包含图片
        if (status.pic_urls != null && status.pic_urls.size() > 0) {
            viewHolder.initImageView(viewHolder.rl_pics, viewHolder.iv_arrays, status.pic_urls);
        } else {
            if (viewHolder.iv_arrays != null)
                viewHolder.rl_pics.setVisibility(View.GONE);
        }
        //判断此微博是否为转发微博
        if (status.retweeted_status != null) {
            processRetweeted(viewHolder, status);
            viewHolder.ll_item.addView(viewHolder.view_retweeted);
            //设置监听器
            viewHolder.tv_retweeted_comment_count.setOnClickListener(viewHolder.onClickListener);
            viewHolder.tv_retweeted_repost_count.setOnClickListener(viewHolder.onClickListener);
        } else {
            if (viewHolder.view_retweeted != null)
                viewHolder.ll_item.removeView(viewHolder.view_retweeted);
        }
        //判断此微博有没有地理位置

        if (status.annotations != null && status.annotations.get(0).place != null && status.annotations.get(0).place.title != null) {
            viewHolder.tv_location.setVisibility(View.VISIBLE);
            viewHolder.tv_location.setText(status.annotations.get(0).place.title);
        } else {
            viewHolder.tv_location.setVisibility(View.GONE);
        }
        //设置监听器
        viewHolder.tv_screen_name.setOnClickListener(viewHolder.onClickListener);
        viewHolder.iv_avatar.setOnClickListener(viewHolder.onClickListener);

        viewHolder.tv_comment_count.setOnClickListener(viewHolder.onClickListener);
        viewHolder.tv_repost_count.setOnClickListener(viewHolder.onClickListener);
        viewHolder.iv_overflow.setOnClickListener(viewHolder.onClickListener);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onItemLongClick(v, position);
                    return true;
                }
                return false;
            }
        });

    }

    // item上各组件的点击事件监听器
    class WeiboWidghtOnClickListener implements View.OnClickListener {

        private boolean isReweeted;
        private Status status;
        private MyOnMenuItemClickListener onMenuItemClickListener;

        public WeiboWidghtOnClickListener(int position, MyOnMenuItemClickListener onMenuItemClickListener) {
            status = list.get(position);
            this.onMenuItemClickListener = onMenuItemClickListener;
            isReweeted = (status.retweeted_status != null);
        }

        @Override
        public void onClick(View v) {
            if (status == null) {
                return;
            }
            int requestCode;
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.tv_screen_name: //点击用户昵称

                case R.id.iv_avatar: //点击用户头像事件
                    intent.setClass(context, UserDetailsActivity.class);
                    intent.putExtra("User",status.user);
                    context.startActivity(intent);
                    break;
                case R.id.tv_comment_count: //点击评论数事件
                    requestCode = MainActivity.SEND_NEW_COMMENT;
                    intent.setClass(context, PublishActivity.class);
                    intent.putExtra("type", PublishActivity.SEND_COMMENT);
                    intent.putExtra("idstr", status.idstr);
                    intent.putExtra("isReweeted", isReweeted);
                    switchToActivity(intent, requestCode);
                    break;
                case R.id.tv_repost_count: //点击转发数事件
                    requestCode = MainActivity.SEND_NEW_REPOST;
                    intent.setClass(context, PublishActivity.class);
                    intent.putExtra("type", PublishActivity.SEND_REPOST);
                    intent.putExtra("idstr", status.idstr);
                    if (isReweeted) {
                        intent.putExtra("text", "//@" + status.user.screen_name + context.getResources().getString(R.string.colon) + status.text);
                    }
                    switchToActivity(intent, requestCode);
                    break;
                case R.id.iv_overflow:  //点击菜单图标事件
                    if (status.favorited) {
                        CommonUtil.showPopupMenu(context, v, R.menu.overflow_popupmenu_02, onMenuItemClickListener);
                    } else {
                        CommonUtil.showPopupMenu(context, v, R.menu.overflow_popupmenu, onMenuItemClickListener);
                    }
                    break;
                case R.id.tv_retweeted_comment_count: //点击被转发微博的评论数事件
                    requestCode = MainActivity.SEND_NEW_COMMENT;
                    intent.setClass(context, PublishActivity.class);
                    intent.putExtra("type", PublishActivity.SEND_COMMENT);
                    intent.putExtra("idstr", status.retweeted_status.idstr);
                    switchToActivity(intent, requestCode);
                    break;
                case R.id.tv_retweeted_repost_count: //点击被转发微博的转发数事件
                    requestCode = MainActivity.SEND_NEW_REPOST;
                    intent.setClass(context, PublishActivity.class);
                    intent.putExtra("type", PublishActivity.SEND_REPOST);
                    intent.putExtra("idstr", status.retweeted_status.idstr);
                    switchToActivity(intent, requestCode);
                    break;
            }
        }

        // 选择源Activity
        private void switchToActivity(Intent intent, int requestCode) {
            if (context instanceof MainActivity) {
                ((MainActivity) context).startActivityForResult(intent, requestCode);
            } else if (context instanceof NearlyDynamicActivity) {
                ((NearlyDynamicActivity) context).startActivityForResult(intent, requestCode);
            } else if (context instanceof MyFavoriteActivity) {
                ((MyFavoriteActivity) context).startActivityForResult(intent, requestCode);
            }
        }
    }

    // item上的overflow弹出的PopupMenu的点击监听器
    class MyOnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private Status status;
        private ProgressDialog mProgressDialog;

        public MyOnMenuItemClickListener(int position) {
            status = list.get(position);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            LogUtils.i(status + status.user.screen_name);
            switch (item.getItemId()) {
                case R.id.overflow_share:
                    // TODO: 2015/10/4 share the weibo by sharesdk
                    showShare();
                    break;
                case R.id.overflow_favorite:
                    processFavorite();
                    break;
                case R.id.overflow_cancel_favorite:
                    processFavorite();
                    break;
                case R.id.overflow_copy:
                    CommonUtil.copyToClipboard(context,status.text);
                    CommonUtil.showSnackbar(view, R.string.copy_weibo_to_clipboard, context.getResources().getColor(R.color.Indigo_colorPrimary));
                    break;
            }
            return true;
        }

        private void showShare() {
            ShareSDK.initSDK(context);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle(status.user.screen_name);
//            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            oks.setText(status.text);
//            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//            oks.setImagePath(status.pic_urls.get(0).thumbnail_pic);//确保SDcard下面存在此张图片
//            // url仅在微信（包括好友和朋友圈）中使用
//            oks.setUrl("http://sharesdk.cn");
//            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//            oks.setComment("我是测试评论文本");
//            // site是分享此内容的网站名称，仅在QQ空间使用
//            oks.setSite(context.getResources().getString(R.string.app_name));
//            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//            oks.setSiteUrl("http://sharesdk.cn");
            // 启动分享GUI
            oks.show(context);
        }

        private void processFavorite() {
            String url;
            if (status.favorited) {
                url = AppConstant.FAVORITE_DESTROY_URL;
            } else {
                url = AppConstant.FAVORITE_CREATE_URL;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }
                    CommonUtil.showSnackbar(view, R.string.operation_success, context.getResources().getColor(R.color.Indigo_colorPrimary));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    // TODO: 2015/10/4  请求错误
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap();
                    map.put("id", status.idstr);
                    map.put("access_token", SharePrefUtil.getString(context, "access_token", ""));
                    return map;
                }
            };
            mQueue.add(stringRequest);
            LogUtils.i("收藏微博url：" + AppConstant.FAVORITE_CREATE_URL + " , id=" + status.idstr);
            mProgressDialog = CommonUtil.showProgressDialog(context, R.string.please_wait, true);
        }

    }


    // 处理被转发的View
    private void processRetweeted(RecyclerView.ViewHolder holder, Status status) {
        WeiboViewHolder viewHolder = (WeiboViewHolder) holder;
        if (viewHolder.view_retweeted == null) {
            viewHolder.view_retweeted = LayoutInflater.from(context).inflate(R.layout.weibo_retweeted_item, null);
            viewHolder.initRetweetedView();
        }

        //设置被转发微博内容
        //判断该被转发微博是否被删除
        if (!TextUtils.isEmpty(status.retweeted_status.deleted) && status.retweeted_status.deleted.equals("1")) {
            SpannableString weiBoContent = StringUtils.getWeiBoContent(context, status.retweeted_status.text, viewHolder.tv_retweeted_name_text);
            viewHolder.tv_retweeted_name_text.setText(weiBoContent);
        } else {
            SpannableString weiBoContent = StringUtils.getWeiBoContent(context, AT + status.retweeted_status.user.name + context.getResources().getString(R.string.colon) + status.retweeted_status.text, viewHolder.tv_retweeted_name_text);
            viewHolder.tv_retweeted_name_text.setText(weiBoContent);
        }


        if (status.retweeted_status.pic_urls != null && status.retweeted_status.pic_urls.size() > 0) {
            viewHolder.initImageView(viewHolder.rl_retweeted_pics, viewHolder.iv_retweeted_arrays, status.retweeted_status.pic_urls);
        } else {
            if (viewHolder.iv_retweeted_arrays != null)
                viewHolder.rl_retweeted_pics.setVisibility(View.GONE);
        }
        //设置被转发微博 转发数和评论数
        viewHolder.tv_retweeted_repost_count.setText(CommonUtil.getNumString(status.retweeted_status.reposts_count));
        viewHolder.tv_retweeted_comment_count.setText(CommonUtil.getNumString(status.retweeted_status.comments_count));

        ViewParent parent = viewHolder.view_retweeted.getParent();
        if (parent != null) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(viewHolder.view_retweeted);
        }
    }

    /**
     * 设置成 “没有更多微博了”
     */
    public void setNoMoreWeibo() {
        completeLoadMore(false);
        loadSuccess = true;
        if (mFooterViewHolder != null)
            mFooterViewHolder.tv_load_fail.setText(context.getResources().getString(R.string.no_more_weibo));
    }

    public void initFooterViewHolder() {
        if (mFooterViewHolder != null) {
            mFooterViewHolder.tv_load_fail.setVisibility(View.INVISIBLE);
            mFooterViewHolder.ll_load_more.setVisibility(View.VISIBLE);
        }
    }

    // weibo item ViewHolder
    public class WeiboViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout ll_item;
        public TextView tv_screen_name;
        public ImageView iv_avatar;
        public TextView tv_time;
        public TextView tv_device;
        public ImageView iv_verified;
        public TextView tv_repost_count;
        public TextView tv_comment_count;
        public ImageView iv_overflow;
        public TextView tv_text;
        public List<ImageView> iv_arrays = new ArrayList<>();
        public List<ImageView> iv_retweeted_arrays = new ArrayList<>();
        public TextView tv_retweeted_name_text;
        public View view_retweeted;
        public TextView tv_retweeted_repost_count;
        public TextView tv_retweeted_comment_count;
        public TextView tv_location;
        public RelativeLayout rl_pics;
        public RelativeLayout rl_retweeted_pics;
        public WeiboWidghtOnClickListener onClickListener;
        public MyOnMenuItemClickListener onMenuItemClickListener;

        private void initImageView(RelativeLayout rl, List<ImageView> iv_arrays, List<Pic_urls> pic_urls) {
            rl.setVisibility(View.VISIBLE);
            for (int i = 0; i < iv_arrays.size(); i++) {
                if (i < pic_urls.size()) {
                    iv_arrays.get(i).setVisibility(View.VISIBLE);
                    imageLoader.displayImage(pic_urls.get(i).thumbnail_pic, iv_arrays.get(i), ninepic_options);
                } else {
                    iv_arrays.get(i).setVisibility(View.GONE);
                }
            }
        }

        private void initRetweetedView() {
            tv_retweeted_name_text = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_name_text);
            tv_retweeted_repost_count = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_repost_count);
            tv_retweeted_comment_count = (TextView) view_retweeted.findViewById(R.id.tv_retweeted_comment_count);
            rl_retweeted_pics = (RelativeLayout) view_retweeted.findViewById(R.id.rl_pics);
            for (int i = 0; i < IMAGEVIEW_IDS.length; i++) {
                ImageView iv = (ImageView) view_retweeted.findViewById(IMAGEVIEW_IDS[i]);
                iv_retweeted_arrays.add(iv);
            }
        }

        public WeiboViewHolder(View itemView) {
            super(itemView);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_device = (TextView) itemView.findViewById(R.id.tv_device);
            iv_verified = (ImageView) itemView.findViewById(R.id.iv_verified);
            tv_repost_count = (TextView) itemView.findViewById(R.id.tv_repost_count);
            tv_comment_count = (TextView) itemView.findViewById(R.id.tv_comment_count);
            iv_overflow = (ImageView) itemView.findViewById(R.id.iv_overflow);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            rl_pics = (RelativeLayout) itemView.findViewById(R.id.rl_pics);
            for (int i = 0; i < IMAGEVIEW_IDS.length; i++) {
                ImageView iv = (ImageView) itemView.findViewById(IMAGEVIEW_IDS[i]);
                iv_arrays.add(iv);
            }
        }

    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.listener = mOnItemClickListener;
    }

}