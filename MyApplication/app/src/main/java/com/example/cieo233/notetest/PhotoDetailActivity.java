package com.example.cieo233.notetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 1/24/2017.
 */

public class PhotoDetailActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photoViewPager)
    ViewPager photoViewPager;
    @BindView(R.id.bottomMenu)
    RelativeLayout bottomMenu;
    @BindView(R.id.bottomMenuShare)
    ImageView bottomMenuShare;
    @BindView(R.id.bottomMenuDelete)
    ImageView bottomMenuDelete;
    @BindView(R.id.bottomMenuMoveTo)
    ImageView bottomMenuMoveTo;

    private Menu toolbarMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);

        setToolbar();

        Intent intent = getIntent();
        String currentFolder = intent.getStringExtra("currentFolder");
        int currentPosition = intent.getIntExtra("currentPosition",0);
        photoViewPager.setAdapter(new PhotoViewPagerAdapter(this,currentFolder));
        photoViewPager.setCurrentItem(currentPosition);
    }

    void setToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_detail, menu);
        this.toolbarMenu = menu;
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

        }
        return true;
    }
}
