package com.example.anirudhbharti.newsapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.example.anirudhbharti.newsapp.MyApplication;
import com.example.anirudhbharti.newsapp.R;
import com.example.anirudhbharti.newsapp.Utils;
import com.example.anirudhbharti.newsapp.di.component.ApplicationComponent;
import com.example.anirudhbharti.newsapp.di.component.DaggerHomeActivityComponent;
import com.example.anirudhbharti.newsapp.di.component.HomeActivityComponent;
import com.example.anirudhbharti.newsapp.model.Article;
import com.example.anirudhbharti.newsapp.model.NewsResponseModel;
import com.example.anirudhbharti.newsapp.di.module.HomeActivityContextModule;
import com.example.anirudhbharti.newsapp.di.module.HomeActivityMvpModule;
import com.example.anirudhbharti.newsapp.di.qualifier.ActivityContext;
import com.example.anirudhbharti.newsapp.di.qualifier.ApplicationContext;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeActivity extends AppCompatActivity implements HomeActivityContract.View, NewsAdapter.OnItemClickListener {

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    @Inject
    @ApplicationContext
    public Context context;
    @Inject
    @ActivityContext
    public Context activityContext;
    HomeActivityComponent homeActivityComponent;
    @Inject
    HomePresenterImpl homePresenter;
    @Inject
    NewsAdapter newsAdapter;
    @BindView(R.id.rvNews)
    RecyclerView rvNews;
    @BindView(R.id.pbLoader)
    ProgressBar pbLoader;
    @BindView(R.id.llIntenetError)
    LinearLayout llIntenetError;
    @BindView(R.id.tvTryAgain)
    TextView tvTryAgain;
    private RecyclerView.LayoutManager layoutManager;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus, newsCaching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ApplicationComponent applicationComponent = MyApplication.get(this).getApplicationComponent();
        homeActivityComponent = DaggerHomeActivityComponent.builder()
                .homeActivityContextModule(new HomeActivityContextModule(this))
                .homeActivityMvpModule(new HomeActivityMvpModule(this))
                .applicationComponent(applicationComponent)
                .build();
        homeActivityComponent.injectHomeActivity(this);

        layoutManager = new LinearLayoutManager(this);
        rvNews.setLayoutManager(layoutManager);
        rvNews.setAdapter(newsAdapter);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);


        checkWriteStoragePermissionGranted();


        newsAdapter.setOnItemClickListener(this);

    }

    private void getNewsData() {
        if (Utils.checkIfInternetAvialable(this)) {
            homePresenter.loadNewsData("us", Utils.Api_KEY);
        } else {
            try {

                String news = PreferenceManager.getDefaultSharedPreferences(context).getString("newsFeed", "");


                if (!news.isEmpty()) {
                    List<Article> newsList = LoganSquare.parseList(news, Article.class);
                    if (newsList != null && newsList.size() > 0) {
                        newsAdapter.addItem(newsList);
                    } else {
                        llIntenetError.setVisibility(View.VISIBLE);

                    }
                } else {
                    llIntenetError.setVisibility(View.VISIBLE);

                }
            } catch (IOException e) {
                e.printStackTrace();
                llIntenetError.setVisibility(View.VISIBLE);
            }
        }

    }


    @Override
    public void showNewsList(NewsResponseModel newsResponseModel) {
        if (newsResponseModel.articles != null && newsResponseModel.articles.size() > 0) {
            newsAdapter.addItem(newsResponseModel.articles);
            try {
                String news =
                        LoganSquare.serialize(newsResponseModel.articles);
                PreferenceManager.getDefaultSharedPreferences(context).edit().putString("newsFeed", news).apply();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showError(String call, String message) {
        if (message != null)
            Utils.showToast(getApplicationContext(), message);
    }

    @Override
    public void showProgress() {
        pbLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLoader.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(String url) {
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.putExtra("postUrl", url);
        startActivity(intent);
    }

    @OnClick(R.id.tvTryAgain)
    public void onViewClicked() {
        if (Utils.checkIfInternetAvialable(this)) {
            llIntenetError.setVisibility(View.GONE);
            homePresenter.loadNewsData("us", Utils.Api_KEY);
        } else {
            llIntenetError.setVisibility(View.VISIBLE);
        }
    }


    public void checkWriteStoragePermissionGranted() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });

                builder.setCancelable(false);
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        checkWriteStoragePermissionGranted();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            proceedAfterPermission();
        }
    }


    private void proceedAfterPermission() {
        getNewsData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            checkWriteStoragePermissionGranted();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission();
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                proceedAfterPermission();
            } else {
                checkWriteStoragePermissionGranted();
            }
        }
    }
}
