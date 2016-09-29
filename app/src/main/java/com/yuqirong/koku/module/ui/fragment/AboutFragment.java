package com.yuqirong.koku.module.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuqirong.koku.R;
import com.yuqirong.koku.util.CommonUtil;
import com.yuqirong.koku.module.ui.weidgt.AboutTextView;

/**
 * Created by Administrator on 2015/11/3.
 */
public class AboutFragment extends BaseFragment {

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, null);
        AboutTextView atv_share = (AboutTextView) view.findViewById(R.id.atv_share);
        AboutTextView atv_grade = (AboutTextView) view.findViewById(R.id.atv_grade);
        AboutTextView atv_address = (AboutTextView) view.findViewById(R.id.atv_address);
        AboutTextView atv_version = (AboutTextView) view.findViewById(R.id.atv_version);
        atv_version.setContent(CommonUtil.getVersionName(context));
        atv_version.setOnClickListener(listener);
        atv_address.setOnClickListener(listener);
        atv_grade.setOnClickListener(listener);
        atv_share.setOnClickListener(listener);
        return view;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.atv_share:
                    shareApp();
                    break;
                case R.id.atv_address:
                    visitGit();
                    break;
                case R.id.atv_grade:
                    gradeApp();
                    break;
            }
        }
    };

    private void visitGit() {
        Uri uri = Uri.parse("https://github.com/yuqirong/Koku");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void gradeApp() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }

}
