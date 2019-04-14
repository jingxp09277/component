/*
 * ************************************************************
 * 文件：HtmlFragment.java  模块：hybrid-core  项目：component
 * 当前修改时间：2019年04月13日 08:43:55
 * 上次修改时间：2019年04月12日 21:41:40
 * 作者：Cody.yi   https://github.com/codyer
 *
 * 描述：hybrid-core
 * Copyright (c) 2019
 * ************************************************************
 */

package com.cody.component.hybrid.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.cody.component.app.fragment.SingleBindFragment;
import com.cody.component.hybrid.JsBridge;
import com.cody.component.hybrid.R;
import com.cody.component.hybrid.core.JsWebChromeClient;
import com.cody.component.hybrid.core.UrlUtil;
import com.cody.component.hybrid.data.HtmlConfig;
import com.cody.component.hybrid.data.HtmlViewData;
import com.cody.component.hybrid.databinding.FragmentHtmlBinding;
import com.cody.component.hybrid.handler.JsHandlerCommonImpl;
import com.cody.component.image.ImageViewDelegate;
import com.cody.component.image.OnImageViewListener;
import com.cody.component.lib.view.Refreshable;
import com.cody.component.lib.view.Scrollable;
import com.cody.component.util.LogUtil;
import com.lzy.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Html 页面具体实现
 */
public class HtmlFragment extends SingleBindFragment<FragmentHtmlBinding, HtmlViewData>
        implements OnImageViewListener, JsWebChromeClient.OpenFileChooserCallBack, Scrollable, Refreshable {
    private static final String HTML_URL = "html_url";
    private HtmlViewData mHtmlViewData;
    private ImageViewDelegate mImageViewDelegate;
    private ValueCallback<Uri[]> mFilePathCallback;
    private ValueCallback<Uri> mUploadMsg;

    public HtmlFragment() {
        // Required empty public constructor
    }

    public static HtmlFragment getInstance(String url) {
        HtmlFragment fragment = new HtmlFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HTML_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_html;
    }

    @Override
    protected void onBaseReady(final Bundle savedInstanceState) {
        super.onBaseReady(savedInstanceState);
        if (!isBound()) return;
        mImageViewDelegate = new ImageViewDelegate(this);
        JsBridge.getInstance()
                .addJsHandler(JsHandlerCommonImpl.class.getSimpleName(), JsHandlerCommonImpl.class)
                // TODO syncCookie
//                .syncCookie(this, mHtmlViewData.getUrl().getValue(), Repository.getLocalMap(BaseLocalKey.X_TOKEN))
                .setFileChooseCallBack(this)
                .build(getBinding().webView, getViewData());

        if (null != savedInstanceState) {
            getBinding().webView.restoreState(savedInstanceState);
        } else {
            getViewData().setProgress(0);
            getBinding().webView.loadUrl(getViewData().getUrl().getValue());
        }
    }

    @Override
    protected HtmlViewData getViewData() {
        if (mHtmlViewData != null) return mHtmlViewData;
        mHtmlViewData = new HtmlViewData();

        Bundle bundle = getArguments();
        if (bundle != null) {
            String url = bundle.getString(HTML_URL);
            if (url != null) {
                mHtmlViewData.setUrl(url);
            } else {
                finish();
            }
        }
        return mHtmlViewData;
    }

    @Override
    public void onResume() {
        super.onResume();
        JsBridge.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        JsBridge.onPause();
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        LogUtil.d("OpenFileChooserCallBack acceptType=" + acceptType);
        mUploadMsg = uploadMsg;
        if (mImageViewDelegate != null) {
            mImageViewDelegate.pickImage(1, false);
        }
    }

    @Override
    public void showFileChooserCallBack(final ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        LogUtil.d("OpenFileChooserCallBack fileChooserParams=" + fileChooserParams);
        mFilePathCallback = filePathCallback;
        if (mImageViewDelegate != null) {
            mImageViewDelegate.pickImage(1, false);
        }
    }

    @Override
    public void onPreview(int id, List<ImageItem> images) {
    }

    @Override
    public void onPickImage(int id, List<ImageItem> images) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (mUploadMsg == null || images == null || images.size() < 1) {
                return;
            }
            String url = images.get(0).path;
            if (TextUtils.isEmpty(url) || !new File(url).exists()) {
                return;
            }
            Uri uri = Uri.fromFile(new File(url));
            mUploadMsg.onReceiveValue(uri);
            mUploadMsg = null;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // for android 5.0+
            if (mFilePathCallback == null || images == null || images.size() < 1) {
                return;
            }

            Uri[] uris = new Uri[images.size()];
            for (int i = 0; i < images.size(); i++) {
                if (TextUtils.isEmpty(images.get(i).path) || !new File(images.get(i).path).exists()) {
                    continue;
                }
                uris[i] = Uri.fromFile(new File(images.get(i).path));
            }
            mFilePathCallback.onReceiveValue(uris);
            mFilePathCallback = null;
        }
    }

    @Override
    public void onDestroy() {
        JsBridge.onDestroy();
        releaseFileChoose();
        mImageViewDelegate = null;
        super.onDestroy();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        releaseFileChoose();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JsBridge.onActivityResult(requestCode, resultCode, data);
        if (mImageViewDelegate != null) {
            mImageViewDelegate.onActivityResult(requestCode, resultCode, data);
        }
        releaseFileChoose();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getBinding().webView.saveState(outState);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.refresh) {
            refresh();
        } else if (i == R.id.ignore) {
            getViewData().setIgnoreError(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        JsBridge.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void releaseFileChoose() {
        if (mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;
        }

        if (mFilePathCallback != null) {         // for android 5.0+
            mFilePathCallback.onReceiveValue(null);
            mFilePathCallback = null;
        }
    }

    public boolean canGoBack() {
        if (getBinding().webView.canGoBack()) {
            getBinding().webView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void scrollToTop() {
        getBinding().webView.scrollTo(0, 0);
    }

    @Override
    public void refresh() {
        getViewData().setError(false);
        getViewData().setProgress(0);
        getBinding().webView.loadUrl(getViewData().getUrl().getValue());
    }
}