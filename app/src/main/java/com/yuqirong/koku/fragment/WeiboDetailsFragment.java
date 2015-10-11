package com.yuqirong.koku.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuqirong.koku.R;
import com.yuqirong.koku.entity.Pic_urls;
import com.yuqirong.koku.entity.Status;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.DateUtils;
import com.yuqirong.koku.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anyway on 2015/9/18.
 */
public class WeiboDetailsFragment extends BaseFragment {

    public LinearLayout ll_item;
    public TextView tv_screen_name;
    public ImageView iv_avatar;
    public TextView tv_time;
    public TextView tv_device;
    public ImageView iv_verified;
    public ImageView iv_overflow;
    public TextView tv_repost_count;
    public TextView tv_comment_count;
    public TextView tv_text;
    public List<ImageView> iv_arrays = new ArrayList<>();
    public List<ImageView> iv_retweeted_arrays = new ArrayList<>();
    public TextView tv_retweeted_name_text;
    public View view_retweeted;
    public TextView tv_retweeted_repost_count;
    public TextView tv_retweeted_comment_count;
    public RelativeLayout rl_pics;
    public RelativeLayout rl_retweeted_pics;
    private Status status;
    private static final int[] IMAGEVIEW_IDS = new int[]{R.id.iv_01, R.id.iv_02, R.id.iv_03, R.id.iv_04, R.id.iv_05, R.id.iv_06, R.id.iv_07, R.id.iv_08, R.id.iv_09};
    public static final String AT = "@";
    private TextView tv_favorite;
    private RadioButton rb_repost;
    private RadioButton rb_comment;
    private FrameLayout mFrameLayout;
    private FragmentManager fragmentManager;
    private RelativeLayout mRelativeLayout;

    private WeiboDetailsCommentFragment commentFragment;
    private WeiboDetailsRepostFragment repostFragment;

    @Override
    public void initData(Bundle savedInstanceState) {
        fragmentManager = getFragmentManager();
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            status = (Status) intent.getSerializableExtra("Status");
        }
        initWeiboData(status);
        commentFragment = new WeiboDetailsCommentFragment(mRelativeLayout);
        Bundle bundle = new Bundle();
        bundle.putString("idstr", status.idstr);
        commentFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.mFrameLayout, commentFragment, "commentFragment").commit();
    }

    private void initWeiboData(Status status) {
        ll_item.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        tv_screen_name.setText(status.user.name);
        bitmapUtils.display(iv_avatar, status.user.profile_image_url);
        tv_time.setText(DateUtils.getWeiboDate(status.created_at));
        tv_device.setText(Html.fromHtml(status.source));
        //设置认证图标
        switch (status.user.verified_type) {
            case 0:
                iv_verified.setImageResource(R.drawable.avatar_vip);
                break;
            case -1:
                iv_verified.setImageResource(android.R.color.transparent);
                break;
            case 220:
                iv_verified.setImageResource(R.drawable.avatar_grassroot);
                break;
            default:
                iv_verified.setImageResource(R.drawable.avatar_enterprise_vip);
                break;
        }
        //隐藏微博 转发数和评论数
        tv_repost_count.setVisibility(View.GONE);
        tv_comment_count.setVisibility(View.GONE);
        iv_overflow.setVisibility(View.GONE);

        //设置微博内容
        SpannableString weiBoContent = StringUtils.getWeiBoContent(context, status.text, tv_text);
        tv_text.setText(weiBoContent);

        if (status.pic_urls != null && status.pic_urls.size() > 0) {
            initImageView(rl_pics, iv_arrays, status.pic_urls);
        }
        //设置被转发的内容
        if (status.retweeted_status != null) {
            processRetweeted();
            ll_item.addView(view_retweeted);
        }
        rb_comment.setText(rb_comment.getText().toString() + CommonUtil.getNumString(status.comments_count));
        rb_repost.setText(rb_repost.getText().toString() + CommonUtil.getNumString(status.reposts_count));
        tv_favorite.setText(CommonUtil.getNumString(status.attitudes_count));
        rb_comment.setOnCheckedChangeListener(onCheckedChangeListener);
        rb_repost.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // TODO: 2015/10/11
//            if (buttonView.getId() == R.id.rb_comment && isChecked) {
//                LogUtils.i("comment button : " + isChecked);
//                if (commentFragment == null) {
//                    commentFragment = new WeiboDetailsCommentFragment(mRelativeLayout);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("idstr", status.idstr);
//                    commentFragment.setArguments(bundle);
//                }
//                fragmentManager.beginTransaction().replace(R.id.mFrameLayout, commentFragment, "commentFragment").commit();
//            } else if (buttonView.getId() == R.id.rb_repost && isChecked) {
//                LogUtils.i("repost button : " + isChecked);
//                if (repostFragment == null) {
//                    repostFragment = new WeiboDetailsRepostFragment();
//                }
//                fragmentManager.beginTransaction().replace(R.id.mFrameLayout, repostFragment, "repostFragment").commit();
//            }
        }
    };

    // 处理被转发的View
    private void processRetweeted() {
        view_retweeted = LayoutInflater.from(context).inflate(R.layout.weibo_retweeted_item, null);
        initRetweetedView();
        SpannableString weiBoContent = StringUtils.getWeiBoContent(context, AT + status.retweeted_status.user.name + context.getResources().getString(R.string.colon) + status.retweeted_status.text, tv_retweeted_name_text);
        tv_retweeted_name_text.setText(weiBoContent);

        if (status.retweeted_status.pic_urls != null && status.retweeted_status.pic_urls.size() > 0) {
            initImageView(rl_retweeted_pics, iv_retweeted_arrays, status.retweeted_status.pic_urls);
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
        //设置被转发微博 转发数和评论数
        tv_retweeted_repost_count.setText(CommonUtil.getNumString(status.retweeted_status.reposts_count));
        tv_retweeted_comment_count.setText(CommonUtil.getNumString(status.retweeted_status.comments_count));
    }

    private void initImageView(RelativeLayout rl, List<ImageView> iv_arrays, List<Pic_urls> pic_urls) {
        rl.setVisibility(View.VISIBLE);
        for (int i = 0; i < iv_arrays.size(); i++) {
            if (i < pic_urls.size()) {
                iv_arrays.get(i).setVisibility(View.VISIBLE);
                bitmapUtils.display(iv_arrays.get(i), pic_urls.get(i).thumbnail_pic);
            } else {
                iv_arrays.get(i).setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_weibo_details, null);
        ll_item = (LinearLayout) view.findViewById(R.id.ll_item);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.mRelativeLayout);
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        tv_screen_name = (TextView) view.findViewById(R.id.tv_screen_name);
        iv_overflow = (ImageView) view.findViewById(R.id.iv_overflow);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_device = (TextView) view.findViewById(R.id.tv_device);
        iv_verified = (ImageView) view.findViewById(R.id.iv_verified);
        tv_repost_count = (TextView) view.findViewById(R.id.tv_repost_count);
        tv_comment_count = (TextView) view.findViewById(R.id.tv_comment_count);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
        rl_pics = (RelativeLayout) view.findViewById(R.id.rl_pics);
        rb_comment = (RadioButton) view.findViewById(R.id.rb_comment);
        rb_repost = (RadioButton) view.findViewById(R.id.rb_repost);
        tv_favorite = (TextView) view.findViewById(R.id.tv_favorite);
        mFrameLayout = (FrameLayout) view.findViewById(R.id.mFrameLayout);

        for (int i = 0; i < IMAGEVIEW_IDS.length; i++) {
            ImageView iv = (ImageView) view.findViewById(IMAGEVIEW_IDS[i]);
            iv_arrays.add(iv);
        }

        return view;
    }
}
