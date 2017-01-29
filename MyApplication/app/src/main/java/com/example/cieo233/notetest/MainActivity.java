package com.example.cieo233.notetest;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.AutoTransition;
import com.transitionseverywhere.TransitionManager;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener, Interfaces.OnFolderClickedListener, Interfaces.OnImageClickedListener, View.OnClickListener {
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
    @BindView(R.id.drawerLayoutContent)
    RelativeLayout drawerLayoutContent;


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
        GlobalStorage.getInstance().getImageFromContentProvider(this);
        setToolbar();
        setRecyclerView();
    }


    void notifyDatasetChange() {
        drawerRecyclerViewAdapter.updateDateset();
        imageRecyclerViewAdapter.updateDateset(currentFolder);
        allImageBadge.setText(GlobalStorage.getInstance().getFolderCount("allImage"));
    }

    void init() {
        currentFolder = "allImage";
        selectMode = false;
        allImageButton.setOnClickListener(this);
        popUpMenuDelete.setOnClickListener(this);
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
        DragItemTouchHelper dragItemTouchHelper = new DragItemTouchHelper(new DragItemTouchHelperCallback(new DragItemTouchHelperCallback.OnItemTouchCallbackListener() {
            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
                if (GlobalStorage.getInstance().getImageFolder(currentFolder).getImageInfoList() != null){
                    Collections.swap(GlobalStorage.getInstance().getImageFolder(currentFolder).getImageInfoList(),srcPosition,targetPosition);
                    imageRecyclerViewAdapter.notifyItemMoved(srcPosition,targetPosition);
                    return true;
                }
                return false;
            }
        }));
        dragItemTouchHelper.attachToRecyclerView(drawerLayoutContentRecyclerView);
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
                selectMode = !selectMode;
                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setDuration(100);
                if (selectMode) {
                    item.setTitle("取消");
                    TransitionManager.beginDelayedTransition(drawerLayoutContent, autoTransition);
                    popUpMenu.setVisibility(View.VISIBLE);
                } else {
                    item.setTitle("选择");
                    for (View checkBox : GlobalStorage.getInstance().getSelectedImageViewCheckBox()) {
                        checkBox.setVisibility(View.GONE);
                    }
                    TransitionManager.beginDelayedTransition(drawerLayoutContent, autoTransition);
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
            Intent intent = new Intent(this,PhotoDetailActivity.class);
            intent.putExtra("imageURL",clickedImageInfo.getImageURL());
            intent.putExtra("currentFolder",currentFolder);
            intent.putExtra("currentPosition",GlobalStorage.getInstance().getImageFolder(currentFolder).getImageInfoList().indexOf(clickedImageInfo));
            Log.e("index", String.valueOf(GlobalStorage.getInstance().getImageFolder(currentFolder).getImageInfoList().indexOf(clickedImageInfo)));
            startActivity(intent);

        } else {

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.allImageButton:
                currentFolder = "allImage";
                imageRecyclerViewAdapter.setImageFolder(currentFolder);
                imageRecyclerViewAdapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.popUpMenuDelete:
                GlobalStorage.getInstance().deleteSelected(getApplicationContext());
                selectMode = !selectMode;
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
                popUpMenu.setVisibility(View.GONE);
                toolbarMenu.findItem(R.id.toolbarSelect).setTitle("选择");
                notifyDatasetChange();
                break;
        }
    }

}
