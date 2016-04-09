package com.methodica.lizalinto.dummyinbox;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListPopupWindow;

public class DetailNewsActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String url = intent.getStringExtra("detailLink");
        WebView webView = (WebView)findViewById(R.id.webView);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait. Loading...");
        mProgressDialog.show();

        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
    }



  class MyWebViewClient extends WebViewClient {

      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
          view.loadUrl(url);

          if (!mProgressDialog.isShowing()) {
              mProgressDialog.show();
          }

          return true;
      }

      @Override
      public void onPageFinished(WebView view, String url) {
          System.out.println("on finish");
          if (mProgressDialog.isShowing()) {
              mProgressDialog.dismiss();
          }

      }
  }
}

