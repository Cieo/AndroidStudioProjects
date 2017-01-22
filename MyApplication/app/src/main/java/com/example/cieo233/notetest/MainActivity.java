package com.example.cieo233.notetest;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, Interfaces.OnFolderClickedListener, Interfaces.OnImageClickedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.drawerLayoutContentRecyclerView)
    RecyclerView drawerLayoutContentRecyclerView;
    @BindView(R.id.drawerLayoutDrawerRecyclerView)
    RecyclerView drawerLayoutDrawerRecyclerView;
    @BindView(R.id.allImageButton)
    Button allImageButton;
    @BindView(R.id.allImageBadge)
    TextView allImageBadge;
    @BindView(R.id.popUpMenu)
    RelativeLayout popUpMenu;
    @BindView(R.id.popUpMenuShare)
    ImageView popUpMenuShare;
    @BindView(R.id.popUpMenuDelete)
    ImageView popUpMenuDelete;
    @BindView(R.id.popUpMenuMoveTo)
    TextView popUpMenuMoveTo;

    private boolean selectMode;
    private Menu toolbarMenu;
    String currentFolder;

    private ImageRecyclerViewAdapter imageRecyclerViewAdapter;
    private DrawerRecyclerViewAdapter drawerRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        getImageFromContentProvider();
        setToolbar();
        setRecyclerView();
    }


    void notifyDatasetChange(){
        drawerRecyclerViewAdapter.updateDateset();
        imageRecyclerViewAdapter.updateDateset(currentFolder);
        allImageBadge.setText(GlobalStorage.getInstance().getFolderCount("allImage"));
    }

    void init() {
        currentFolder = "allImage";
        selectMode = false;
        allImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFolder = "allImage";
                imageRecyclerViewAdapter.setImageFolder(currentFolder);
                imageRecyclerViewAdapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        popUpMenuDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalStorage.getInstance().deleteSelected(getApplicationContext());
                selectMode = !selectMode;
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
                popUpMenu.setVisibility(View.GONE);
                toolbarMenu.findItem(R.id.toolbarSelect).setTitle("选择");
                notifyDatasetChange();
            }
        });
    }

    void setToolbar() {
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

    void setRecyclerView() {
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(this);
        imageRecyclerViewAdapter.setOnImageClickedListener(this);
        imageRecyclerViewAdapter.setImageFolder("allImage");
        drawerRecyclerViewAdapter = new DrawerRecyclerViewAdapter(this);
        drawerRecyclerViewAdapter.setOnFolderClickedListener(this);
        drawerLayoutDrawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayoutDrawerRecyclerView.setAdapter(drawerRecyclerViewAdapter);
        drawerLayoutContentRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        drawerLayoutContentRecyclerView.setAdapter(imageRecyclerViewAdapter);
        allImageBadge.setText(GlobalStorage.getInstance().getFolderCount("allImage"));
    }

    void getImageFromContentProvider() {
        GlobalStorage.getInstance().getImageFolders().clear();

        ContentResolver contentResolver = getContentResolver();
        String path = "/storage/emulated/0/";
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media.DATA + " like ?", new String[]{"%" + path + "%"}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String imageURL = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String folderImageIn = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (GlobalStorage.getInstance().getImageFolders().containsKey(folderImageIn)) {
                    GlobalStorage.getInstance().getImageFolders().get(folderImageIn).getImageInfoList().add(new ImageInfo(imageURL, folderImageIn));
                } else {
                    GlobalStorage.getInstance().getImageFolders().put(folderImageIn, new ImageFolder(folderImageIn));
                    GlobalStorage.getInstance().getImageFolders().get(folderImageIn).getImageInfoList().add(new ImageInfo(imageURL, folderImageIn));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.toolbarMenu = menu;
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbarSelect:
                Toast.makeText(this, "Share is clicked", Toast.LENGTH_SHORT).show();
                selectMode = !selectMode;
                if (selectMode) {
                    item.setTitle("取消");
                    popUpMenu.setVisibility(View.VISIBLE);
                } else {
                    item.setTitle("选择");
                    for (View checkBox : GlobalStorage.getInstance().getSelectedImageViewCheckBox()) {
                        checkBox.setVisibility(View.GONE);
                    }
                    popUpMenu.setVisibility(View.GONE);
                    GlobalStorage.getInstance().clearSeletecd();
                }
                break;
        }
        return true;
    }

    @Override
    public void onFolderClicked(ImageFolder clickedFolder) {
        currentFolder = clickedFolder.getFolderName();
        imageRecyclerViewAdapter.setImageFolder(clickedFolder.getFolderName());
        imageRecyclerViewAdapter.notifyDataSetChanged();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onImageClicked(ImageInfo clickedImageInfo, View checkBox) {
        if (!selectMode) {
            return;
        }
        GlobalStorage.getInstance().getSelectedImageInfo().add(clickedImageInfo);
        if (checkBox.getVisibility() == View.VISIBLE) {
            checkBox.setVisibility(View.GONE);
            GlobalStorage.getInstance().getSelectedImageViewCheckBox().remove(checkBox);
        } else {
            checkBox.setVisibility(View.VISIBLE);
            GlobalStorage.getInstance().getSelectedImageViewCheckBox().add(checkBox);
        }
    }
}
