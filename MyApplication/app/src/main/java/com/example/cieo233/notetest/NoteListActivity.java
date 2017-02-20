package com.example.cieo233.notetest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.noteediter.NoteRichEditor;
import com.transitionseverywhere.AutoTransition;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 2/10/2017.
 */

public class NoteListActivity extends AppCompatActivity implements Interfaces.OnNoteFolderClickedListener,  View.OnClickListener, Interfaces.OnNoteClickedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.drawerLayoutContentRecyclerView)
    RecyclerView drawerLayoutContentRecyclerView;
    @BindView(R.id.drawerLayoutDrawerRecyclerView)
    RecyclerView drawerLayoutDrawerRecyclerView;
    @BindView(R.id.allImageButton)
    Button allNoteButton;
    @BindView(R.id.allImageBadge)
    TextView allNoteBadge;
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
    @BindView(R.id.jumpToSlide)
    LinearLayout jumpToSlide;
    @BindView(R.id.addNewNotebook)
    LinearLayout addNewNotebook;

    private int choosed,srcPosition, upX, upY, lastX, lastY;


    private boolean selectMode;
    String currentFolder;

    private NoteRecyclerViewAdapter noteRecyclerViewAdapter;
    private NoteDrawerRecyclerViewAdapter noteDrawerRecyclerViewAdapter;
    private NoteDatabaseHelper noteDatabaseHelper;
    private Button selectedDrawer;
    private Dialog bottomDialog;
    private Dialog dialog;
    private EditText createNewFolderEditText;
    private TextView createNewFolderCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);
        init();
        noteDatabaseHelper = new NoteDatabaseHelper(this,"note",null,1);
        noteDatabaseHelper.clear();
        noteDatabaseHelper.insert(new NoteInfo("null","testFolder","testTime","testName1"));
        noteDatabaseHelper.insert(new NoteInfo("null","testFolder","testTime","testName2"));
        noteDatabaseHelper.insert(new NoteInfo("null","testFolder","testTime","testName3"));
        noteDatabaseHelper.insert(new NoteInfo("null","testFolder3","testTime","testName4"));
        noteDatabaseHelper.insert(new NoteInfo("null","testFolder4","testTime","testName5"));
        noteDatabaseHelper.insert(new NoteInfo("null","testFolder1","testTime","testName3"));
        noteDatabaseHelper.insert(new NoteInfo("null","testFolder2","testTime","testName4"));
        noteDatabaseHelper.insert(new NoteInfo("null","testFolder","testTime","testName5"));

        GlobalStorage.getInstance().getNoteFromDataBase(this);
        setToolbar();
        setRecyclerView();
    }


    void notifyDatasetChange() {
        noteDrawerRecyclerViewAdapter.updateDateset();
        noteRecyclerViewAdapter.updateDateset(currentFolder);
        allNoteBadge.setText(GlobalStorage.getInstance().getNoteFolderCount("allNote"));
    }

    void init() {
        srcPosition = -1;
        choosed = -1;
        currentFolder = "allNote";
        selectMode = false;
        allNoteButton.setOnClickListener(this);
        popUpMenuDelete.setOnClickListener(this);
        jumpToSlide.setOnClickListener(this);
        popUpMenuShare.setOnClickListener(this);
        addNewNotebook.setOnClickListener(this);
        selectedDrawer = (Button) findViewById(R.id.allImageButton);
        GlobalStorage.getInstance().setSelectedNoteDrawerButton(-1);
        bottomDialog = new Dialog(this,R.style.MaterialDialogSheet);
        setDialog();
    }

    void setDialog(){
        View view = getLayoutInflater().inflate(R.layout.bottom_dialog,null);
        bottomDialog.setContentView(view);
        bottomDialog.setCancelable(true);
        bottomDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarMenu.setOnClickListener(this);
        toolbarSelect.setOnClickListener(this);
    }

    void setRecyclerView() {
        noteDrawerRecyclerViewAdapter = new NoteDrawerRecyclerViewAdapter(this);
        noteDrawerRecyclerViewAdapter.setOnNoteFolderClickedListener(this);
        drawerLayoutDrawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayoutDrawerRecyclerView.setAdapter(noteDrawerRecyclerViewAdapter);
        noteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this);
        noteRecyclerViewAdapter.setOnNoteClickedListener(this);
        allNoteBadge.setText(GlobalStorage.getInstance().getNoteFolderCount("allNote"));
        drawerLayoutContentRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        drawerLayoutContentRecyclerView.setAdapter(noteRecyclerViewAdapter);
        noteRecyclerViewAdapter.setNoteFolder(currentFolder);

        DragItemTouchHelper dragItemTouchHelper = new DragItemTouchHelper(new DragItemTouchHelperCallback(new DragItemTouchHelperCallback.OnItemTouchCallbackListener() {

            @Override
            public void chooseDropTarget(RecyclerView.ViewHolder selected, RecyclerView.ViewHolder winner) {
                srcPosition = selected.getAdapterPosition();
                if (winner == null){
                    if (drawerLayoutContentRecyclerView.findViewHolderForAdapterPosition(choosed) != null){
                        NoteRecyclerViewAdapter.MyViewHolder myViewHolder = (NoteRecyclerViewAdapter.MyViewHolder) drawerLayoutContentRecyclerView.findViewHolderForAdapterPosition(choosed);
                        myViewHolder.getNoteThumbnail().setBackgroundColor(Color.parseColor("#9b9b9b"));
                        choosed = -1;
                    }
                } else {
                    if (winner.getAdapterPosition() == 0){
                        return;
                    }
                    if (choosed != winner.getAdapterPosition()){
                        NoteRecyclerViewAdapter.MyViewHolder myViewHolder = (NoteRecyclerViewAdapter.MyViewHolder) drawerLayoutContentRecyclerView.findViewHolderForAdapterPosition(choosed);
                        if (myViewHolder != null){
                            myViewHolder.getNoteThumbnail().setBackgroundColor(Color.parseColor("#9b9b9b"));
                        }
                        choosed = winner.getAdapterPosition();
                        myViewHolder = (NoteRecyclerViewAdapter.MyViewHolder) winner;
                        myViewHolder.getNoteThumbnail().setBackgroundColor(Color.parseColor("#66ccff"));
                    }

                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == 0){
                    if (choosed != -1){
                        Log.e("TestChoosed", String.valueOf(choosed));
                        NoteRecyclerViewAdapter.MyViewHolder myViewHolder = (NoteRecyclerViewAdapter.MyViewHolder) drawerLayoutContentRecyclerView.findViewHolderForAdapterPosition(choosed);
                        myViewHolder.getNoteThumbnail().setBackgroundColor(Color.parseColor("#9b9b9b"));
                        NoteRecyclerViewAdapter.MyViewHolder srcViewHolder = (NoteRecyclerViewAdapter.MyViewHolder) drawerLayoutContentRecyclerView.findViewHolderForAdapterPosition(srcPosition);
                        srcViewHolder.getNoteThumbnail().setVisibility(View.GONE);
                        noteRecyclerViewAdapter.remove(srcPosition);
                        noteDatabaseHelper.delete(noteRecyclerViewAdapter.getNoteFolder().getNoteInfoList().get(srcPosition-1).getNoteMark());
                        GlobalStorage.getInstance().getNoteFromDataBase(getApplicationContext());
                        allNoteBadge.setText(GlobalStorage.getInstance().getNoteFolderCount("allNote"));
                        noteDrawerRecyclerViewAdapter.updateDateset();
                        // Todo
                        srcPosition = -1;
                        choosed = -1;
                    }
                }
            }


        }));
        dragItemTouchHelper.attachToRecyclerView(drawerLayoutContentRecyclerView);
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
                currentFolder = "allNote";
                noteRecyclerViewAdapter.setNoteFolder(currentFolder);
                noteRecyclerViewAdapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                clearButtonBackground();
                selectedDrawer = (Button) findViewById(R.id.allImageButton);
                GlobalStorage.getInstance().setSelectedNoteDrawerButton(-1);
                setButtonBackground();
                break;
            case R.id.popUpMenuDelete:
                GlobalStorage.getInstance().deleteSelectedNote(getApplicationContext());
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
                    TransitionManager.beginDelayedTransition(drawerLayoutContent, autoTransition);
                    popUpMenu.setVisibility(View.GONE);
                    GlobalStorage.getInstance().clearSeletecdNote();
                }
                break;
            case R.id.toolbarMenu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.jumpToSlide:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.popUpMenuShare:
                bottomDialog.show();
                break;
            case R.id.addNewNotebook:
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
                    noteDatabaseHelper.createNoteFolder(folderName);
                    GlobalStorage.getInstance().getNoteFromDataBase(this);
                    currentFolder = folderName;
                    noteRecyclerViewAdapter.setNoteFolder(currentFolder);
                    notifyDatasetChange();
                    dialog.dismiss();
                }
                break;
        }
    }

    @Override
    public void onFolderClicked(NoteFolder clickedFolder, Button button) {
        currentFolder = clickedFolder.getFolderName();
        noteRecyclerViewAdapter.setNoteFolder(clickedFolder.getFolderName());
        noteRecyclerViewAdapter.notifyDataSetChanged();
        drawerLayout.closeDrawer(GravityCompat.START);
        clearButtonBackground();
        selectedDrawer = button;
        setButtonBackground();
    }

    @Override
    public void onNoteClicked(NoteInfo clickedNote, View checkBox) {
        if (!selectMode) {
            Intent intent = new Intent(this, NoteRichEditor.class);
            intent.putExtra("noteContent",clickedNote.getNoteContent());
            intent.putExtra("noteContent",clickedNote.getNoteContent());
            startActivityForResult(intent,1);
//            intent.putExtra("imageURL",clickedImageInfo.getImageURL());
//            intent.putExtra("currentFolder",currentFolder);
//            intent.putExtra("currentPosition",GlobalStorage.getInstance().getImageFolder(currentFolder).getImageInfoList().indexOf(clickedImageInfo));
//            Log.e("index", String.valueOf(GlobalStorage.getInstance().getImageFolder(currentFolder).getImageInfoList().indexOf(clickedImageInfo)));
//            startActivity(intent);

        } else {
            if (checkBox.getVisibility() == View.VISIBLE) {
                checkBox.setVisibility(View.GONE);
                GlobalStorage.getInstance().getSelectedNoteInfo().remove(clickedNote);
                GlobalStorage.getInstance().getSelectedNoteThumbnailCheckBox().remove(checkBox);
            } else {
                checkBox.setVisibility(View.VISIBLE);
                GlobalStorage.getInstance().getSelectedNoteInfo().add(clickedNote);
                GlobalStorage.getInstance().getSelectedNoteThumbnailCheckBox().add(checkBox);
            }
        }
        Log.e("TestOnClicked","noteClicked");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO
    }

    @Override
    public void onAddOneClicked() {
        Log.e("TestAddOne","AddOneClicked");
    }
}