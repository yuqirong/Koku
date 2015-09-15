package com.yuqirong.koku.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.yuqirong.koku.R;
import com.yuqirong.koku.constant.AppConstant;
import com.yuqirong.koku.util.BitmapUtil;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;
import com.yuqirong.koku.util.SharePrefUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Anyway on 2015/9/13.
 */
public class PublishActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ImageView iv_location;
    private ImageView iv_photo;
    private ImageView iv_emotion;
    private ImageView iv_noti;
    private ImageView iv_sharp;
    private ImageView iv_send;
    private ImageView iv_add;
    private TextView tv_location;
    private EditText et_content;
    private TextView tv_word_num;
    private LinearLayout ll_images;
    private HorizontalScrollView mHorizontalScrollView;
    private Map<ImageView, String> imageMap = new HashMap<>();
    private LoadImageAsyncTask loadImageAsyncTask;
    private boolean isPicture = false;

    /**
     * 是否定位
     */
    private boolean isLocation = false;
    /**
     * 相机拍照
     */
    private static final int TAKE_PHOTO = 1002;
    /**
     * 系统图库
     */
    private static final int TAKE_GALLERY = 1003;
    private double longitude;
    private double latitude;

    public static final int SEND_WEIBO_SUCCESS = 1200;

    @Override
    protected void initData() {

    }

    @Override
    protected void initToolBar() {
        mToolbar.setTitle(R.string.publish_weibo);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
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
        iv_location = (ImageView) findViewById(R.id.iv_location);
        iv_photo = (ImageView) findViewById(R.id.iv_photo);
        iv_emotion = (ImageView) findViewById(R.id.iv_emotion);
        iv_noti = (ImageView) findViewById(R.id.iv_noti);
        iv_sharp = (ImageView) findViewById(R.id.iv_sharp);
        iv_send = (ImageView) findViewById(R.id.iv_send);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_word_num = (TextView) findViewById(R.id.tv_word_num);
        et_content.addTextChangedListener(watcher);
        iv_location.setOnClickListener(listener);
        iv_photo.setOnClickListener(listener);
        iv_emotion.setOnClickListener(listener);
        iv_noti.setOnClickListener(listener);
        iv_sharp.setOnClickListener(listener);
        iv_send.setOnClickListener(listener);
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
                case R.id.iv_location:
                    processLocation();
                    break;
                case R.id.iv_photo:
                    processPhoto();
                    break;
                case R.id.iv_sharp:
                    processTopic();
                    break;
                case R.id.iv_add:
                    processPhoto();
                    break;
                case R.id.iv_send:
                    processSendWeibo(v);
                    break;
            }
        }
    };

    // 发布微博
    private void processSendWeibo(View v) {
        String content = et_content.getText().toString();
        if (TextUtils.isEmpty(content.trim())) {
            Snackbar.make(v, R.string.content_cannot_be_empty, Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (imageMap.size() > 0) {

        } else {
            final String encoderContent = content;
//                        URLEncoder.encode(content, "UTF-8");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.STATUSES_UPDATE_URL, sendWeiboListener, errorListener) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("access_token", SharePrefUtil.getString(PublishActivity.this, "access_token", ""));
                    map.put("status", encoderContent);
                    if (isLocation) {
                        map.put("lat", String.valueOf(latitude));
                        map.put("long", String.valueOf(longitude));
                    }
                    return map;
                }
            };
            mQueue.add(stringRequest);
        }

    }

    Response.Listener<String> sendWeiboListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String idstr = jsonObject.getString("idstr");
                if (!TextUtils.isEmpty(idstr)) {
                    setResult(RESULT_OK);
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
    private void processPhoto() {
        if (imageMap.size() > 8) {
            CommonUtil.showToast(PublishActivity.this, R.string.no_more_pictures);
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
                        intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
                        startActivityForResult(intent, TAKE_GALLERY);
                        break;
                }
            }
        });
    }

    // 处理点击 # 事件
    private void processTopic() {
        et_content.setText(et_content.getText().toString() + "##");
        et_content.setSelection(et_content.getText().toString().length() - 1);
    }

    class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;
        private String url;
        private int imageWidth;
        private int imageHeight;

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
                imageView.setImageBitmap(bitmap);
                imageMap.put(imageView, url);
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
            imageView = new ImageView(PublishActivity.this);
            imageView.setBackgroundResource(R.drawable.bg_card_item_light);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ll_images.addView(imageView, ll_images.getChildCount() - 1);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
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
                    Uri uri = data.getData();
                    Cursor cursor = getContentResolver().query(uri, null,
                            null, null, null);
                    cursor.moveToFirst();
                    String imgPath = cursor.getString(1); // 图片文件路径
                    loadImageAsyncTask = new LoadImageAsyncTask();
                    loadImageAsyncTask.execute(imgPath);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
