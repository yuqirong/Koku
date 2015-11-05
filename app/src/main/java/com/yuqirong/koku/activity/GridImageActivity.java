package com.yuqirong.koku.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yuqirong.koku.R;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hanks on 2015/9/14.
 */
public class GridImageActivity extends Activity {

    private View view_layer;
    private TextView tv_title;
    private RecyclerView recycler_image;
    private RecyclerView recycler_dir;
    private ImageAdapter imageAdapter;
    private TextView tv_finish;

    private ArrayList<Floder> mDirPaths = new ArrayList();

    /**
     * 已选择的图片
     */
    private ArrayList<String> selectedPicture = new ArrayList();

    private Floder imageAll, currentImageFolder;
    private ImageLoader loader;
    private DisplayImageOptions options;
    private FolderAdapter dirAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_picture);
        bindViews();
        initViews();
        bindListeners();
    }

    public void back(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void select(View v) {
        if (selectedPicture.size() == 0) {
            CommonUtil.showSnackbar(recycler_image, R.string.no_pic, getResources().getColor(R.color.Indigo_colorPrimary));
            return;
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra("urls", selectedPicture);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void bindViews() {
        view_layer = findViewById(R.id.view_layer);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        recycler_image = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_dir = (RecyclerView) findViewById(R.id.recycler_dir);
        view_layer.setVisibility(View.GONE);
    }

    private void initViews() {
        loader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image).showImageOnFail(R.drawable.default_image)
                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).build();

        imageAll = new Floder();
        imageAll.setDir("/所有图片");
        currentImageFolder = imageAll;
        mDirPaths.add(imageAll);
        getThumbnail();
    }

    private void bindListeners() {
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDirlistPop();
            }
        });

        //图片列表
        GridLayoutManager mgr = new GridLayoutManager(this, 3);
        recycler_image.setLayoutManager(mgr);
        recycler_image.addItemDecoration(new GridSpacingItemDecoration(3, dp2px(5), false));
        imageAdapter = new ImageAdapter();
        recycler_image.setAdapter(imageAdapter);

        //目录列表
        recycler_dir.setLayoutManager(new LinearLayoutManager(this));
        dirAdapter = new FolderAdapter();
        recycler_dir.setAdapter(dirAdapter);

        //黑色图层
        view_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDirList();
            }
        });
    }

    boolean isDirShowing = false;

    private void toggleDirlistPop() {
        if (isDirShowing) {
            hideDirList();
        } else {
            showDirlist();
        }
    }

    private void hideDirList() {
        view_layer.animate().alpha(0).setDuration(300).start();

        recycler_dir.animate().translationY(-dp2px(310)).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isDirShowing = false;
                view_layer.setVisibility(View.GONE);
            }
        }).start();
    }

    private void showDirlist() {
        view_layer.setVisibility(View.VISIBLE);
        view_layer.animate().alpha(1).setDuration(300).start();
        recycler_dir.animate().translationY(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isDirShowing = true;
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (isDirShowing) {
            hideDirList();
            isDirShowing = false;
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        /**
         * 临时的辅助类，用于防止同一个文件夹的多次扫描
         */
        HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();

        Cursor mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        Log.e("TAG", mCursor.getCount() + "");
        if (mCursor.moveToFirst()) {
            do {
                // 获取图片的路径
                String path = mCursor.getString(0);
                Log.e("TAG", path);
                imageAll.images.add(new ImageItem(path));
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                Floder imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new Floder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        for (int i = 0; i < mDirPaths.size(); i++) {
            Floder f = mDirPaths.get(i);
            Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
        }
        tmpDir = null;
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ImageViewHolder holder, final int position) {
            final ImageItem item = currentImageFolder.images.get(position);
            loader.displayImage("file://" + item.path, holder.imageView, options);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mCheckBox.setChecked(!holder.mCheckBox.isChecked());
                }
            });
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        addAnimation(holder.mCheckBox);
                        if (selectedPicture.size() >= 9) {
                            CommonUtil.showSnackbar(recycler_image, R.string.most_nine_pic, getResources().getColor(R.color.Indigo_colorPrimary));
                            buttonView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    buttonView.setChecked(false);
                                }
                            }, 300);

                            return;
                        }
                        selectedPicture.add(item.path);
                    } else {
                        selectedPicture.remove(item.path);
                    }
                    String content = tv_finish.getText().toString();
                    tv_finish.setText(content.substring(0, content.indexOf("/") - 1) +
                            selectedPicture.size() + content.substring(content.indexOf("/"), content.length()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return currentImageFolder.images.size();
        }

        /**
         * 给CheckBox加点击动画
         *
         * @param view
         */
        private void addAnimation(View view) {
            float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
            AnimatorSet set = new AnimatorSet();
            set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                    ObjectAnimator.ofFloat(view, "scaleY", vaules));
            set.setDuration(150);
            set.start();
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public CheckBox mCheckBox;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.mCheckBox);
        }

    }


    class FolderAdapter extends RecyclerView.Adapter<FolderViewHolder> {
        @Override
        public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dir, parent, false);
            return new FolderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FolderViewHolder holder, int position) {
            final Floder item = mDirPaths.get(position);
            loader.displayImage("file://" + item.getFirstImagePath(), holder.iv_dir, options);
//            hanks.com.mylibrary.util.ImageLoader.getInstance().loadImage(item.getFirstImagePath(),holder.iv_dir);
            holder.tv_dirname.setText(item.name + " (" + item.images.size() + "张) ");
            holder.ll_root.setSelected(currentImageFolder == item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetDirList(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDirPaths.size();
        }
    }

    private void resetDirList(Floder selectFolder) {
        currentImageFolder = selectFolder;
        dirAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
        hideDirList();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_dir;
        public TextView tv_dirname;
        public View ll_root;

        public FolderViewHolder(View itemView) {
            super(itemView);
            ll_root = itemView.findViewById(R.id.ll_root);
            iv_dir = (ImageView) itemView.findViewById(R.id.iv_dir);
            tv_dirname = (TextView) itemView.findViewById(R.id.tv_dirname);
        }
    }


    class Floder {


        /**
         * 图片的文件夹路径
         */
        private String dir;
        /**
         * 第一张图片的路径
         */
        private String firstImagePath;

        /**
         * 文件夹的名称
         */
        private String name;
        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }

    }

    class ImageItem {

        String path;

        public ImageItem(String p) {
            this.path = p;
        }

    }

    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }


    }

    public int dp2px(int dp) {
        float scale = getResources().getDisplayMetrics().densityDpi;
        return (int) (dp * scale / 160 + 0.5f);
    }

}
