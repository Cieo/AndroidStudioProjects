package com.example.cieo233.notetest;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView, contentRecyclerView;
    private HashMap<String, ImageFolder> imageFolders;
    private ImageFolder allImage;
    private Button button;
    private TextView badge;
    private Interfaces.OnFolderClickedListener onFolderClickedListener;
    private ImageRecyclerViewAdapter imageRecyclerViewAdapter;
    private DrawerRecyclerViewAdapter drawerRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getImage();
        setToolbar();
        setRecyclerView();
    }

    void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        contentRecyclerView = (RecyclerView) findViewById(R.id.contentRecyclerView);
        button = (Button) findViewById(R.id.button);
        badge = (TextView) findViewById(R.id.badge);
        imageFolders = new HashMap<>();
        allImage = new ImageFolder("所有幻灯片");
        onFolderClickedListener = new Interfaces.OnFolderClickedListener() {
            @Override
            public void onFolderClicked(ImageFolder clickedFolder) {
                imageRecyclerViewAdapter.setImageFolder(clickedFolder);
                imageRecyclerViewAdapter.notifyDataSetChanged();
                Log.e("TestOnclick",clickedFolder.getFolderName()+"Clicked");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageRecyclerViewAdapter.setImageFolder(allImage);
                imageRecyclerViewAdapter.notifyDataSetChanged();
                Log.e("TestOnclick","AllImageClicked");
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
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
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(this,allImage);
        drawerRecyclerViewAdapter = new DrawerRecyclerViewAdapter(this, imageFolders);
        drawerRecyclerViewAdapter.setOnFolderClickedListener(onFolderClickedListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(drawerRecyclerViewAdapter);
        contentRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        contentRecyclerView.setAdapter(imageRecyclerViewAdapter);
        button.setText(allImage.getFolderName());
        badge.setText(String.valueOf(allImage.getFolderCount()));
    }

    void getImage(){

        ContentResolver contentResolver = getContentResolver();
        Uri targetUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String path = "/storage/emulated/0/";
        Cursor cursor = contentResolver.query(targetUri, null, MediaStore.Images.Media.DATA + " like ?", new String[]{"%"+path+"%"}, null);
        if (cursor!= null){
            while (cursor.moveToNext()){
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String folder = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                Log.e("TestContent", url);
                Log.e("TestContent", folder);
                allImage.getUrlList().add(url);
                if (imageFolders.containsKey(folder)){
                    imageFolders.get(folder).getUrlList().add(url);
                } else {
                    imageFolders.put(folder,new ImageFolder(folder));
                    imageFolders.get(folder).getUrlList().add(url);
                }
            }
        }
        Log.e("TestContent", String.valueOf(imageFolders.size()));
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
