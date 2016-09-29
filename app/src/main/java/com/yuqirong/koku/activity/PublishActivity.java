package com.yuqirong.koku.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.adapter.EmotionAdapter;
import com.yuqirong.koku.application.MyApplication;
import com.yuqirong.koku.app.AppConstant;
import com.yuqirong.koku.db.DraftDB;
import com.yuqirong.koku.entity.Draft;
import com.yuqirong.koku.entity.Emotion;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;
import com.yuqirong.koku.view.DeleteImageView;
import com.yuqirong.koku.view.RevealBackgroundView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * 发布微博 评论微博 转发微博等
 * Created by Anyway on 2015/9/13.
 */
public class PublishActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener {

    private Toolbar mToolbar;
    private ActionBar actionBar;
    private ImageButton ib_location;
    private ImageButton ib_photo;
    private ImageButton ib_emotion;
    private ImageButton ib_noti;
    private ImageButton ib_sharp;
    private ImageButton ib_send;
    private ImageView iv_add;
    private TextView tv_location;
    private EditText et_content;
    private CheckBox cb_comment_to_auth;
    private TextView tv_word_num;
    private LinearLayout ll_images;
    private HorizontalScrollView mHorizontalScrollView;
    private Map<DeleteImageView, String> imageMap = new HashMap<>();
    private List<String> strList = new ArrayList<>();
    private LoadImageAsyncTask loadImageAsyncTask;
    private GridView gv_emotion;
    private EmotionAdapter adapter;
    private RevealBackgroundView vRevealBackground;

    public static final int SEND_WEIBO = 1010; //发布微博
    public static final int SEND_COMMENT = 1020; //发布评论
    public static final int SEND_REPOST = 1030; //转发微博
    public static final int REFRESH_DRAFT = 1040; //刷新草稿箱
    public static final int REPLY_COMMENT = 1050; //回复评论

    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    private static final int TAKE_PHOTO = 1002; //相机拍照
    private static final int TAKE_GALLERY = 1003; //系统图库

    private boolean isPicture = false; //是否发含有图片的微博
    private boolean isLocation = false; //是否定位
    private double longitude; //经度
    private double latitude; //维度
    private int type; //源Activity 类型
    private String url; //网络url
    private String idstr; //微博ID

    public static final int SEND_WEIBO_SUCCESS = 1200; //发微博成功
    public static final int SEND_COMMENT_SUCCESS = 1250; //评论成功
    public static final int SEND_REPOST_SUCCESS = 1300; //转发成功

    private LinearLayout mLinearLayout;
    private boolean isFromFAButton; //是否从含有FloatingActionButton的界面跳转过来
    private int draft_id; //草稿箱ID
    private String cid; // 回复某人评论时的评论ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (type == PublishActivity.SEND_WEIBO && isFromFAButton) {
            setupRevealBackground(savedInstanceState);
        } else {
            mLinearLayout.setBackgroundColor(getResources().getColor(R.color.activity_bg_color));
            vRevealBackground.setVisibility(View.GONE);
        }
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setFillPaintColor(getResources().getColor(R.color.Indigo_colorPrimary));
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return false;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            if (RevealBackgroundView.STATE_FINISHED == state) {
                vRevealBackground.setVisibility(View.GONE);
                mLinearLayout.setBackgroundColor(getResources().getColor(R.color.activity_bg_color));
            }
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        draft_id = intent.getIntExtra("draft_id", 0);
        type = intent.getIntExtra("type", 0);
        isFromFAButton = intent.getBooleanExtra("isFromFAButton", false);
        idstr = intent.getStringExtra("idstr");
        cid = intent.getStringExtra("cid");
        String text = intent.getStringExtra("text");
        if (text != null) {
            et_content.setText(text);
        }
        switch (type) {
            case SEND_WEIBO:   //发微博
                actionBar.setTitle(R.string.publish_weibo);
                url = AppConstant.STATUSES_UPDATE_URL;
                break;
            case SEND_COMMENT:  //评论
                boolean isReweeted = intent.getBooleanExtra("isReweeted", false);
                actionBar.setTitle(R.string.comment_weibo);
                ib_location.setVisibility(View.GONE);
                ib_photo.setVisibility(View.GONE);
                et_content.setHint(R.string.comment_weibo);
                if (isReweeted) {
                    cb_comment_to_auth.setVisibility(View.VISIBLE);
                    cb_comment_to_auth.setText(R.string.comment_to_original_weibo);
                }
                url = AppConstant.COMMENTS_CREATE_URL;
                break;
            case SEND_REPOST:  //转发
                actionBar.setTitle(R.string.repost_weibo);
                ib_location.setVisibility(View.GONE);
                ib_photo.setVisibility(View.GONE);
                ib_sharp.setVisibility(View.GONE);
                cb_comment_to_auth.setVisibility(View.VISIBLE);
                et_content.setHint(R.string.repost_weibo);
                et_content.setSelection(0);
                url = AppConstant.STATUSES_REPOST_URL;
                break;
            case REPLY_COMMENT:
                actionBar.setTitle(R.string.reply_comment);
                ib_location.setVisibility(View.GONE);
                ib_photo.setVisibility(View.GONE);
                cb_comment_to_auth.setVisibility(View.VISIBLE);
                et_content.setHint(R.string.reply_comment);
                url = AppConstant.COMMENTS_REPLY_URL;
                break;
            default:
                break;
        }

    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_publish);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.mHorizontalScrollView);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        et_content = (EditText) findViewById(R.id.et_content);
        ll_images = (LinearLayout) findViewById(R.id.ll_images);
        ib_location = (ImageButton) findViewById(R.id.ib_location);
        ib_photo = (ImageButton) findViewById(R.id.ib_photo);
        ib_emotion = (ImageButton) findViewById(R.id.ib_emotion);
        ib_noti = (ImageButton) findViewById(R.id.ib_noti);
        ib_sharp = (ImageButton) findViewById(R.id.ib_sharp);
        ib_send = (ImageButton) findViewById(R.id.ib_send);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_word_num = (TextView) findViewById(R.id.tv_word_num);
        gv_emotion = (GridView) findViewById(R.id.gv_emotion);
        cb_comment_to_auth = (CheckBox) findViewById(R.id.cb_comment_to_auth);
        vRevealBackground = (RevealBackgroundView) findViewById(R.id.vRevealBackground);
        mLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayout);

        adapter = new EmotionAdapter(this, new ArrayList<Emotion>());
        gv_emotion.setAdapter(adapter);
        gv_emotion.setOnItemClickListener(onItemClickListener);
        et_content.addTextChangedListener(watcher);
        et_content.setOnClickListener(listener);
        ib_location.setOnClickListener(listener);
        ib_photo.setOnClickListener(listener);
        ib_emotion.setOnClickListener(listener);
        ib_noti.setOnClickListener(listener);
        ib_sharp.setOnClickListener(listener);
        ib_send.setOnClickListener(listener);
        iv_add.setOnClickListener(listener);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String content = s.toString();
            String num = tv_word_num.getText().toString();
            tv_word_num.setText(content.length() + num.substring(num.indexOf("/"), num.length()));
        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_location:
                    processLocation();
                    break;
                case R.id.ib_photo:
                    processPhoto(v);
                    break;
                case R.id.ib_emotion:
                    processEmotion();
                    break;
                case R.id.ib_noti:
                    processNoti();
                    break;
                case R.id.ib_sharp:
                    processTopic();
                    break;
                case R.id.iv_add:
                    processPhoto(v);
                    break;
                case R.id.ib_send:
                    processSendWeibo(v);
                    break;
                case R.id.et_content:
                    if (gv_emotion.isShown()) {
                        gv_emotion.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Emotion emotion = adapter.getList().get(position);
            Bitmap bitmap = BitmapFactory.decodeByteArray(emotion.getData(), 0, emotion.getData().length);
            if (bitmap != null) {
                SpannableString ss = new SpannableString(emotion.getKey());
                ImageSpan imageSpan = new ImageSpan(PublishActivity.this, bitmap, ImageSpan.ALIGN_BOTTOM);
                ss.setSpan(imageSpan, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                et_content.getText().insert(et_content.getSelectionEnd(), ss);
            }
        }
    };

    // 处理 @ 事件
    private void processNoti() {
        Intent intent = new Intent(this, SearchUserActivity.class);
        intent.putExtra("type", SearchUserActivity.AT_USER);
        startActivityForResult(intent, SearchUserActivity.AT_USER);
    }

    //选择表情
    private void processEmotion() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(PublishActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (gv_emotion.isShown()) {
            gv_emotion.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gv_emotion.setVisibility(View.GONE);
                }
            }, 300);
        } else {
            gv_emotion.setVisibility(View.VISIBLE);
        }
    }

    // 发布微博
    private void processSendWeibo(View v) {
        if (gv_emotion.isShown()) {
            gv_emotion.setVisibility(View.GONE);
        }
        final String content = et_content.getText().toString();
        if (TextUtils.isEmpty(content.trim()) && type != SEND_REPOST) {
            CommonUtil.showSnackbar(v, R.string.content_cannot_be_empty, getResources().getColor(R.color.Indigo_colorPrimary));
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, sendWeiboListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap();
                map.put("access_token", SharePrefUtil.getString(PublishActivity.this, "access_token", ""));
                map.put("status", content);
                switch (type) {
                    case SEND_WEIBO:
                        map.put("status", content);
                        if (isLocation) {
                            map.put("lat", String.valueOf(latitude));
                            map.put("long", String.valueOf(longitude));
                        }
                        break;
                    case SEND_COMMENT:
                        map.put("comment", content);
                        map.put("id", idstr);
                        if (cb_comment_to_auth.isChecked()) {
                            map.put("comment_ori", "1");
                        }
                        break;
                    case SEND_REPOST:
                        map.put("status", content);
                        map.put("id", idstr);
                        if (cb_comment_to_auth.isChecked()) {
                            map.put("is_comment", "1");
                        }
                        break;
                    case REPLY_COMMENT:
                        map.put("comment", content);
                        map.put("cid", cid);
                        map.put("id", idstr);
                        if (cb_comment_to_auth.isChecked()) {
                            map.put("comment_ori", "1");
                        }
                }
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    Response.Listener<String> sendWeiboListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String idstr = jsonObject.getString("idstr");
                if (!TextUtils.isEmpty(idstr)) {
                    if (type == SEND_WEIBO) {
                        setResult(SEND_WEIBO_SUCCESS);
                    } else if (type == SEND_COMMENT) {
                        setResult(SEND_COMMENT_SUCCESS);
                    } else if (type == SEND_REPOST) {
                        setResult(SEND_REPOST_SUCCESS);
                    }
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    // 处理点击 位置 事件
    private void processLocation() {
        Location location = CommonUtil.getLocation(PublishActivity.this);
        if (!isLocation) {
            if (location == null) {
                tv_location.setVisibility(View.VISIBLE);
                tv_location.setText(R.string.unknown_address);
            } else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                isLocation = true;
                LogUtils.i("GPS坐标  longitude :" + longitude + ", latitude :" + latitude);
                String url = AppConstant.GEO_TO_ADDRESS_URL + "?access_token=" + SharePrefUtil.getString(PublishActivity.this, "access_token", "") + "&coordinate=" + longitude + "," + latitude;
                LogUtils.i("根据地理信息坐标返回实际地址 :" + url);
                getJsonData(url, jsonlistener, errorListener);
            }
        } else {
            tv_location.setVisibility(View.GONE);
            isLocation = false;
        }
    }

    Response.Listener<JSONObject> jsonlistener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject json) {
            try {
                JSONArray geo = json.getJSONArray("geos");
                JSONObject geo0 = geo.getJSONObject(0);
                String address = geo0.getString("address");
                LogUtils.i("实际地址 :" + address);
                tv_location.setVisibility(View.VISIBLE);
                tv_location.setText(address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            CommonUtil.showToast(PublishActivity.this, "Network Error");
        }
    };

    private String imageUrl;

    // 处理点击 图片 事件
    private void processPhoto(View v) {
        if (imageMap.size() > 8) {
            Snackbar.make(v, R.string.no_more_pictures, Snackbar.LENGTH_SHORT).show();
            return;
        }
        CommonUtil.createItemAlertDialog(PublishActivity.this, getResources().getStringArray(R.array.photo_items), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    case 0:
                        File DatalDir = Environment.getExternalStorageDirectory();
                        File myDir = new File(DatalDir, "/DCIM/Camera");
                        myDir.mkdirs();
                        String mDirectoryname = DatalDir.toString() + "/DCIM/Camera";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.SIMPLIFIED_CHINESE);
                        File tempfile = new File(mDirectoryname, "IMG_" + sdf.format(new Date()) + ".jpg");//tempfile为图片的路径
                        if (tempfile.isFile())
                            tempfile.delete();
                        intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        imageUrl = tempfile.getAbsolutePath();
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUrl);
                        startActivityForResult(intent, TAKE_PHOTO);
                        break;
                    case 1:
                        String url = CommonUtil.getLatestCameraPicture(PublishActivity.this);
                        loadImageAsyncTask = new LoadImageAsyncTask();
                        loadImageAsyncTask.execute(url);
                        break;
                    case 2:
//                        intent = new Intent(Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
//                        startActivityForResult(intent, TAKE_GALLERY);
                        startActivityForResult(new Intent(PublishActivity.this, GridImageActivity.class), TAKE_GALLERY);
                        break;
                }
            }
        },true);
    }

    // 处理点击 # 事件
    private void processTopic() {
        et_content.getText().append("##");
        et_content.setSelection(et_content.getText().length() - 1);
    }

    class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private DeleteImageView mDeleteImageView;
        private String url;
        private int imageWidth;
        private int imageHeight;
        private View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_images.removeView(mDeleteImageView);
                if (imageMap.containsKey(mDeleteImageView)) {
                    imageMap.remove(mDeleteImageView);
                }
                if(imageMap.size() == 0){
                    mHorizontalScrollView.setVisibility(View.GONE);
                }
            }
        };

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO 优化图片
            url = params[0];
            Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromUrl(url, imageWidth, imageHeight);
            return bitmap;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mDeleteImageView.setImageBitmap(bitmap);
                mDeleteImageView.setOnDeleteClickListener(listener);
                imageMap.put(mDeleteImageView, url);
                strList.add(url);
            }
            if (imageMap.size() > 8) {
                iv_add.setVisibility(View.GONE);
            }
            LogUtils.i("image_url :" + url + ", imageMap_size :" + imageMap.size());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mHorizontalScrollView.getVisibility() != View.VISIBLE) {
                mHorizontalScrollView.setVisibility(View.VISIBLE);
            }
            mDeleteImageView = new DeleteImageView(PublishActivity.this);
            mDeleteImageView.setBackgroundResource(R.drawable.bg_unselected_card_item_light);
            ll_images.addView(mDeleteImageView, ll_images.getChildCount() - 1);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mDeleteImageView.getLayoutParams();
            layoutParams.width = CommonUtil.dip2px(PublishActivity.this, 90);
            layoutParams.height = CommonUtil.dip2px(PublishActivity.this, 90);
            layoutParams.setMargins(CommonUtil.dip2px(PublishActivity.this, 10), 0, CommonUtil.dip2px(PublishActivity.this, 10), 0);
            imageWidth = layoutParams.width;
            imageHeight = layoutParams.height;
            LogUtils.i("imageView" + (ll_images.getChildCount() - 1) + "width :" + imageWidth + " , height :" + imageHeight);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri == null) {
                        if (imageUrl != null) {
                            Bitmap bitmap = data.getParcelableExtra("data");
                            try {
                                OutputStream out = new FileOutputStream(imageUrl);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                out.flush();
                                out.close();
                                loadImageAsyncTask = new LoadImageAsyncTask();
                                loadImageAsyncTask.execute(imageUrl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        String path = uri.getPath();
                        loadImageAsyncTask = new LoadImageAsyncTask();
                        loadImageAsyncTask.execute(path);
                    }
                }
                break;
            case TAKE_GALLERY:
                if (resultCode == RESULT_OK) {
//                    Uri uri = data.getData();
                    List<String> urls = data.getStringArrayListExtra("urls");
//                    Cursor cursor = getContentResolver().query(uri, null,
//                            null, null, null);
//                    cursor.moveToFirst();
//                    String imgPath = cursor.getString(1); // 图片文件路径
                    for (int i = 0; i < urls.size(); i++) {
                        LoadImageAsyncTask loadImageAsyncTask = new LoadImageAsyncTask();
                        loadImageAsyncTask.execute(urls.get(i));
                    }

                }
                break;
            case SearchUserActivity.AT_USER:
                if (resultCode == RESULT_OK) {
                    String screen_name = data.getStringExtra("screen_name");
                    et_content.getText().insert(et_content.getSelectionEnd(), "@" + screen_name + " ");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveToDraft();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveToDraft();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     *返回提示是否保存到草稿箱
     */
    private void saveToDraft() {
        //如果表情框出现，则隐藏
        if (gv_emotion.isShown()) {
            gv_emotion.setVisibility(View.GONE);
            return;
        }
        // 如果是回复评论，直接finish
        if (type == REPLY_COMMENT) {
            finish();
            return;
        }
        final String content = et_content.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            CommonUtil.createMessageAlertDialog(this, getResources().getString(R.string.tip), getResources().getString(R.string.save_draft), getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }, getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyApplication.getExecutor().execute(
                            new Runnable() {
                                @Override
                                public void run() {
                                    if (draft_id == 0) {
                                        Draft d = new Draft(0, type, content, strList, idstr);
                                        DraftDB.addDraft(d);
                                    } else {
                                        Draft d = new Draft(draft_id, type, content, strList, idstr);
                                        DraftDB.updateDraft(d);
                                    }
                                }
                            });
                    if (draft_id != 0) {
                        setResult(REFRESH_DRAFT);
                    }
                    finish();
                }
            }, true);
        } else {
            finish();
        }

    }

}
