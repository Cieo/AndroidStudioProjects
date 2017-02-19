package com.example.cieo233.notetest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.AutoTransition;
import com.transitionseverywhere.TransitionManager;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Interfaces.OnFolderClickedListener, Interfaces.OnImageClickedListener, View.OnClickListener {
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
    @BindView(R.id.toolbarSelect)
    TextView toolbarSelect;
    @BindView(R.id.toolbarMenu)
    ImageView toolbarMenu;
    @BindView(R.id.jumpToNote)
    LinearLayout jumpToNote;
    @BindView(R.id.addNewAlbum)
    LinearLayout addNewAlbum;

    private int choosed,srcPosition, upX, upY, lastX, lastY;


    private boolean selectMode;
    String currentFolder;

    private ImageRecyclerViewAdapter imageRecyclerViewAdapter;
    private DrawerRecyclerViewAdapter drawerRecyclerViewAdapter;
    private NoteDatabaseHelper noteDatabaseHelper;
    private Button selectedDrawer;
    private Dialog dialog;
    private EditText createNewFolderEditText;
    private TextView createNewFolderCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        noteDatabaseHelper = new NoteDatabaseHelper(this,"note",null,1);
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
        noteDatabaseHelper = new NoteDatabaseHelper(this,"note",null,1);
        srcPosition = -1;
        choosed = -1;
        currentFolder = "allImage";
        selectMode = false;
        allImageButton.setOnClickListener(this);
        popUpMenuDelete.setOnClickListener(this);
        jumpToNote.setOnClickListener(this);
        addNewAlbum.setOnClickListener(this);
        selectedDrawer = (Button) findViewById(R.id.allImageButton);
        GlobalStorage.getInstance().setSelectedImageDrawerButton(-1);
    }

    void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarMenu.setOnClickListener(this);
        toolbarSelect.setOnClickListener(this);
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


    @Override
    public void onFolderClicked(ImageFolder clickedFolder, Button button) {
        currentFolder = clickedFolder.getFolderName();
        imageRecyclerViewAdapter.setImageFolder(clickedFolder.getFolderName());
        imageRecyclerViewAdapter.notifyDataSetChanged();
        drawerLayout.closeDrawer(GravityCompat.START);
        clearButtonBackground();
        selectedDrawer = button;
        setButtonBackground();
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
            if (checkBox.getVisibility() == View.VISIBLE) {
                checkBox.setVisibility(View.GONE);
                GlobalStorage.getInstance().getSelectedImageInfo().remove(clickedImageInfo);
                GlobalStorage.getInstance().getSelectedImageViewCheckBox().remove(checkBox);
            } else {
                checkBox.setVisibility(View.VISIBLE);
                GlobalStorage.getInstance().getSelectedImageInfo().add(clickedImageInfo);
                GlobalStorage.getInstance().getSelectedImageViewCheckBox().add(checkBox);
            }
        }

    }

    void clearButtonBackground(){
        if (selectedDrawer == null){
            return;
        }
        selectedDrawer.setBackgroundResource(R.drawable.button_style_white);
    }

    void setButtonBackground(){
        if (selectedDrawer == null){
            return;
        }
        selectedDrawer.setBackgroundResource(R.drawable.button_style_yellow);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.allImageButton:
                currentFolder = "allImage";
                imageRecyclerViewAdapter.setImageFolder(currentFolder);
                imageRecyclerViewAdapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                clearButtonBackground();
                selectedDrawer = (Button) findViewById(R.id.allImageButton);
                GlobalStorage.getInstance().setSelectedImageDrawerButton(-1);
                setButtonBackground();
                break;
            case R.id.popUpMenuDelete:
                GlobalStorage.getInstance().deleteSelected(getApplicationContext());
                selectMode = !selectMode;
                popUpMenu.setVisibility(View.GONE);
                toolbarSelect.setText("选择");
                notifyDatasetChange();
                break;
            case R.id.toolbarSelect:
                selectMode = !selectMode;
                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setDuration(100);
                if (selectMode) {
                    toolbarSelect.setText("取消");
                    TransitionManager.beginDelayedTransition(drawerLayoutContent, autoTransition);
                    popUpMenu.setVisibility(View.VISIBLE);
                } else {
                    toolbarSelect.setText("选择");
                    for (View checkBox : GlobalStorage.getInstance().getSelectedImageViewCheckBox()) {
                        checkBox.setVisibility(View.GONE);
                    }
                    TransitionManager.beginDelayedTransition(drawerLayoutContent, autoTransition);
                    popUpMenu.setVisibility(View.GONE);
                    GlobalStorage.getInstance().clearSeletecd();
                }
                break;
            case R.id.toolbarMenu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.jumpToNote:
                Intent intent = new Intent(this,NoteListActivity.class);
                startActivity(intent);
                break;
            case R.id.addNewAlbum:
                dialog = new Dialog(this);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_one_dialog);
                dialog.getWindow().setLayout(1080, LinearLayout.LayoutParams.WRAP_CONTENT);
                createNewFolderEditText = (EditText) dialog.findViewById(R.id.inputNewFolderName);
                createNewFolderCheck = (TextView) dialog.findViewById(R.id.createNewFolderCheck);
                createNewFolderCheck.setOnClickListener(this);
                dialog.show();
                break;
            case R.id.createNewFolderCheck:
                String folderName = createNewFolderEditText.getText().toString();
                if (!folderName.isEmpty()){
                    noteDatabaseHelper.createImageFolder(folderName);
                    GlobalStorage.getInstance().getImageFromContentProvider(this);
                    currentFolder = folderName;
                    imageRecyclerViewAdapter.setImageFolder(currentFolder);
                    notifyDatasetChange();
                    dialog.dismiss();
                }
                break;
        }
    }

}
