package com.example.cieo233.unittest;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    ViewPager view_pager;
    private FragmentAdapter fragment_adapter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startService(new Intent(this,SyncService.class));
        init();
    }

    void init() {
        fragments = new ArrayList<>();
        fragments.add(new ReminderFragment());
        fragments.add(new ChannelFragment());
        fragment_adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        view_pager.setAdapter(fragment_adapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,SyncService.class));
        super.onDestroy();
    }
}
