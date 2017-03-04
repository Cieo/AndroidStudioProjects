package com.example.cieo233.notetest;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.transitionseverywhere.AutoTransition;
import com.transitionseverywhere.TransitionManager;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Interfaces.OnImageFolderClickedListener, Interfaces.OnImageClickedListener, View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.contentRecyclerView)
    RecyclerView contentRecyclerView;
    @BindView(R.id.drawerRecyclerView)
    RecyclerView drawerRecyclerView;
    @BindView(R.id.showAllImage)
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


    private boolean isSelectMode;
    String currentShowingFolder;

    private ImageContentRecyclerViewAdapter contentRecyclerViewAdapter;
    private ImageDrawerRecyclerViewAdapter drawerRecyclerViewAdapter;
    private SlideNoteDatabaseHelper slideNoteDatabaseHelper;
    private Button highLightedDrawerButton;
    private Dialog createNewFolderDialog;
    private EditText createNewFolderEditText;
    private TextView createNewFolderCheck;
    private TextView createNewFolderCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        slideNoteDatabaseHelper = new SlideNoteDatabaseHelper(this,"note",null,1);
        GlobalStorage.getInstance().getImageFromContentProvider(this);
        setToolbar();
        setRecyclerView();
    }


    void notifyAllAdapterDataSetChange() {
        drawerRecyclerViewAdapter.updateDateSet();
        contentRecyclerViewAdapter.updateDateSet(currentShowingFolder);
        allImageBadge.setText(GlobalStorage.getInstance().getImageFolderSize("allImage"));
        Log.e("TestCurrentShowing",currentShowingFolder);
        if (!Objects.equals(currentShowingFolder, "allImage")){
            final int position = drawerRecyclerViewAdapter.getFolderViewHolderPosition(currentShowingFolder);
            drawerRecyclerView.scrollToPosition(position);
            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    clearHighLightBackground();
                    ImageDrawerRecyclerViewAdapter.MyViewHolder myViewHolder = (ImageDrawerRecyclerViewAdapter.MyViewHolder) drawerRecyclerView.findViewHolderForAdapterPosition(position);
                    highLightedDrawerButton = myViewHolder.getButton();
                    setHighLightBackground();
                    return false;
                }
            });
            handler.sendEmptyMessageDelayed(1,100);
        } else {
            clearHighLightBackground();
            highLightedDrawerButton = allImageButton;
            setHighLightBackground();
        }

    }

    void init() {
        slideNoteDatabaseHelper = new SlideNoteDatabaseHelper(this,"note",null,1);
        currentShowingFolder = "allImage";
        isSelectMode = false;
        allImageButton.setOnClickListener(this);
        popUpMenuDelete.setOnClickListener(this);
        popUpMenuMoveTo.setOnClickListener(this);
        jumpToNote.setOnClickListener(this);
        addNewAlbum.setOnClickListener(this);
        highLightedDrawerButton = (Button) findViewById(R.id.showAllImage);
        GlobalStorage.getInstance().setHighLightedImageDrawerButton(-1);
    }

    void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarMenu.setOnClickListener(this);
        toolbarSelect.setOnClickListener(this);
    }

    void setRecyclerView() {
        contentRecyclerViewAdapter = new ImageContentRecyclerViewAdapter(this);
        contentRecyclerViewAdapter.setOnImageClickedListener(this);
        contentRecyclerViewAdapter.setImageFolder("allImage");
        drawerRecyclerViewAdapter = new ImageDrawerRecyclerViewAdapter(this);
        drawerRecyclerViewAdapter.setOnImageFolderClickedListener(this);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerRecyclerView.setAdapter(drawerRecyclerViewAdapter);
        contentRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        contentRecyclerView.setAdapter(contentRecyclerViewAdapter);
        allImageBadge.setText(GlobalStorage.getInstance().getImageFolderSize("allImage"));
    }


    @Override
    public void onFolderClicked(ImageFolder clickedFolder, Button button) {
        currentShowingFolder = clickedFolder.getFolderName();
        contentRecyclerViewAdapter.setImageFolder(clickedFolder.getFolderName());
        contentRecyclerViewAdapter.notifyDataSetChanged();
        drawerLayout.closeDrawer(GravityCompat.START);
        clearHighLightBackground();
        highLightedDrawerButton = button;
        setHighLightBackground();
    }

    @Override
    public void onImageClicked(ImageInfo clickedImageInfo, View checkBox) {
        if (!isSelectMode) {
            Intent intent = new Intent(this,PhotoDetailActivity.class);
            intent.putExtra("imageURL",clickedImageInfo.getImageURL());
            intent.putExtra("currentShowingFolder", currentShowingFolder);
            intent.putExtra("currentPosition",GlobalStorage.getInstance().getImageFolder(currentShowingFolder).indexOf(clickedImageInfo));
            Log.e("index", String.valueOf(GlobalStorage.getInstance().getImageFolder(currentShowingFolder).indexOf(clickedImageInfo)));
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

    void clearHighLightBackground(){
        if (highLightedDrawerButton == null){
            Log.e("ClearHighLight","there is a null");
            return;
        }
        highLightedDrawerButton.setBackgroundResource(R.drawable.button_style_white);
    }

    void setHighLightBackground(){
        if (highLightedDrawerButton == null){
            return;
        }
        highLightedDrawerButton.setBackgroundResource(R.drawable.button_style_yellow);
    }

    void showDialog(){
        createNewFolderDialog = new Dialog(this);
        createNewFolderDialog.setCancelable(true);
        createNewFolderDialog.setContentView(R.layout.add_one_dialog);
        createNewFolderDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        createNewFolderEditText = (EditText) createNewFolderDialog.findViewById(R.id.inputNewFolderName);
        createNewFolderCheck = (TextView) createNewFolderDialog.findViewById(R.id.createNewFolderCheck);
        createNewFolderCancel = (TextView) createNewFolderDialog.findViewById(R.id.createNewFolderCancel);
        createNewFolderCheck.setOnClickListener(this);
        createNewFolderCancel.setOnClickListener(this);
        createNewFolderDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showAllImage:
                currentShowingFolder = "allImage";
                contentRecyclerViewAdapter.setImageFolder(currentShowingFolder);
                contentRecyclerViewAdapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                clearHighLightBackground();
                highLightedDrawerButton = (Button) findViewById(R.id.showAllImage);
                GlobalStorage.getInstance().setHighLightedImageDrawerButton(-1);
                setHighLightBackground();
                break;
            case R.id.popUpMenuDelete:
                GlobalStorage.getInstance().deleteSelectedImage(getApplicationContext());
                isSelectMode = !isSelectMode;
                popUpMenu.setVisibility(View.GONE);
                toolbarSelect.setText("选择");
                notifyAllAdapterDataSetChange();
                break;
            case R.id.toolbarSelect:
                isSelectMode = !isSelectMode;
                AutoTransition autoTransition = new AutoTransition();
                autoTransition.setDuration(100);
                if (isSelectMode) {
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
                    GlobalStorage.getInstance().clearSelectedImage();
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
                showDialog();
                break;
            case R.id.createNewFolderCheck:
                String folderName = createNewFolderEditText.getText().toString();
                if (!folderName.isEmpty()){
                    slideNoteDatabaseHelper.createImageFolder(folderName);
                    GlobalStorage.getInstance().getImageFromContentProvider(this);
                    currentShowingFolder = folderName;
                    contentRecyclerViewAdapter.setImageFolder(currentShowingFolder);
                    notifyAllAdapterDataSetChange();
                    createNewFolderDialog.dismiss();
                }
                break;
            case R.id.popUpMenuMoveTo:
                if (GlobalStorage.getInstance().getSelectedImageInfo().size() > 0){
                    Intent moveToIntent = new Intent(this,MoveToActivity.class);
                    startActivityForResult(moveToIntent,1);
                }
                break;
            case R.id.createNewFolderCancel:
                createNewFolderDialog.dismiss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                notifyAllAdapterDataSetChange();
                toolbarSelect.callOnClick();
                break;
        }
    }
}
