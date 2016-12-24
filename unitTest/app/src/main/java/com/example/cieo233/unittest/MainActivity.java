package com.example.cieo233.unittest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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
        sharedPreferences = getSharedPreferences("currentUser",0);
        editor = sharedPreferences.edit();
        fragments = new ArrayList<>();
        fragments.add(new ReminderFragment());
        fragments.add(new ChannelFragment());
        fragment_adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        view_pager.setAdapter(fragment_adapter);
    }

    @Override
    public void onBackPressed() {
        Gson gson = new Gson();
        editor.putString("reminders", gson.toJson(CurrentUser.getInstance().getReminders())).apply();
        editor.putString("subscribeChannel", gson.toJson(CurrentUser.getInstance().getSubscribeChannels())).apply();
        editor.putString("unsubscribeChannel", gson.toJson(CurrentUser.getInstance().getUnsubscribeChannels())).apply();
        editor.putString("creatorChannel", gson.toJson(CurrentUser.getInstance().getCreatorChannels())).apply();
        editor.putString("user",gson.toJson(CurrentUser.getInstance().getUser())).apply();

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
