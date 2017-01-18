package com.example.cieo233.notetest;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private List<String> buttonTextList, badgeTextList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setToolbar();
        setRecyclerView();
    }

    void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        buttonTextList = new ArrayList<>();
        badgeTextList = new ArrayList<>();
        buttonTextList.add("交互设计方法");
        buttonTextList.add("大学英语");
        buttonTextList.add("大学物理");
        buttonTextList.add("通信原理");
        badgeTextList.add("10");
        badgeTextList.add("21");
        badgeTextList.add("35");
        badgeTextList.add("6");
    }

    void setToolbar(){
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new DrawerRecyclerViewAdapter(this,buttonTextList,badgeTextList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                Toast.makeText(this,"Share is clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
