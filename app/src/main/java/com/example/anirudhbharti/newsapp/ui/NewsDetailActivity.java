package com.example.anirudhbharti.newsapp.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.anirudhbharti.newsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsDetailActivity extends AppCompatActivity {


    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.main_content)
    ConstraintLayout mainContent;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        webView.getSettings().setJavaScriptEnabled(true);

        if (getIntent().getExtras().containsKey("postUrl"))
            webView.loadUrl(getIntent().getStringExtra("postUrl"));

        webView.setHorizontalScrollBarEnabled(false);

    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }


}

