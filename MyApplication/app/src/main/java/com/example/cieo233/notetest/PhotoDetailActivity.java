package com.example.cieo233.notetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 1/24/2017.
 */

public class PhotoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.photoViewPager)
    ViewPager photoViewPager;
    @BindView(R.id.bottomMenu)
    RelativeLayout bottomMenu;
    @BindView(R.id.toolbarBack)
    ImageView toolbarBack;
    @BindView(R.id.toolbarShare)
    ImageView toolbarShare;
    @BindView(R.id.bottomMenuOCR)
    ImageView bottomMenuOCR;
    @BindView(R.id.bottomMenuCreateNote)
    ImageView bottomMenuCreateNote;
    @BindView(R.id.bottomMenuEdit)
    ImageView bottomMenuEdit;
    @BindView(R.id.bottomMenuDelete)
    ImageView bottomMenuDelete;



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
        toolbarBack.setOnClickListener(this);
        toolbarShare.setOnClickListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbarBack:
                break;
            case R.id.toolbarShare:
                break;
            case R.id.bottomMenuOCR:
                break;
            case R.id.bottomMenuCreateNote:
                break;
            case R.id.bottomMenuEdit:
                break;
            case R.id.bottomMenuDelete:
                break;
        }
    }
}
