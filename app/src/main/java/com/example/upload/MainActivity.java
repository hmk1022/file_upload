package com.example.upload;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mWebSettings;
    public static final int IMAGE_SELECTOR_REQ = 1;
    private ValueCallback mFilePathCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 웹뷰 시작
        mWebView = (WebView) findViewById(R.id.webview);

        setmWebViewFileUploadPossible();



        mWebView.setWebViewClient(new WebViewClient());
        mWebSettings = mWebView.getSettings(); //각종 환경 설정 가능여부
        mWebSettings.setJavaScriptEnabled(true); // 자바스크립트 허용여부
        mWebSettings.setSupportMultipleWindows(false); // 윈도우 여러개 사용여부
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠사이즈 맞추기
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 캐시 허용 여부
        mWebSettings.setUseWideViewPort(true); // wide viewport 사용 여부
        mWebSettings.setSupportZoom(true); // Zoom사용여부
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트가 window.open()사용할수있는지 여부
        mWebSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        mWebSettings.setBuiltInZoomControls(true); // 화면 확대 축소 허용 여부
        mWebSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
        mWebView.loadUrl("http://randomsquiz.s3-website.ap-northeast-2.amazonaws.com"); // 웹뷰 사이트 주소 및 시작



    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }

    /*public void touchUpGoToDestinationButton() {
        final Button button = (Button) findViewById(R.id.goToDestinationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("http://192.168.0.105:8080/egovCourseList.do");
            }
        });

    }*/

    protected void setmWebViewFileUploadPossible() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback filePathCallback, FileChooserParams fileChooserParams) {
                mFilePathCallback = filePathCallback;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                // 여러장의 사진을 선택하는 경우
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Select picture"), IMAGE_SELECTOR_REQ);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECTOR_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    Uri[] uris = new Uri[count];
                    for (int i = 0; i < count; i++) {
                        uris[i] = data.getClipData().getItemAt(i).getUri();
                    }
                    mFilePathCallback.onReceiveValue(uris);
                }
                else if (data.getData() != null) {
                    mFilePathCallback.onReceiveValue((new Uri[]{data.getData()}));
                }
            }
        }
    }









}