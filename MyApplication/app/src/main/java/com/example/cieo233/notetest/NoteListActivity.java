package com.example.cieo233.notetest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.Objects;

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
    @BindView(R.id.contentRecyclerView)
    RecyclerView contentRecyclerView;
    @BindView(R.id.drawerRecyclerView)
    RecyclerView drawerRecyclerView;
    @BindView(R.id.showAllNote)
    Button showAllNote;
    @BindView(R.id.allNoteBadge)
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

    private int toPosition, fromPosition;


    private boolean isSelectMode;
    String currentShowingFolder;

    private NoteContentRecyclerViewAdapter contentRecyclerViewAdapter;
    private NoteDrawerRecyclerViewAdapter drawerRecyclerViewAdapter;
    private SlideNoteDatabaseHelper slideNoteDatabaseHelper;
    private Button highLightedDrawerButton;
    private Dialog bottomShareDialog;
    private Dialog createNewFolderDialog;
    private EditText createNewFolderEditText;
    private TextView createNewFolderCheck;
    private TextView createNewFolderCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);
        init();
        GlobalStorage.getInstance().getNoteFromDataBase(this);
        setDialog();
        setToolbar();
        setRecyclerView();
    }


    void notifyAllAdapterDataSetChange() {
        drawerRecyclerViewAdapter.updateDateSet();
        contentRecyclerViewAdapter.updateDateSet(currentShowingFolder);
        allNoteBadge.setText(GlobalStorage.getInstance().getNoteFolderSize("allNote"));
        if (!Objects.equals(currentShowingFolder, "allNote")){
            final int position = drawerRecyclerViewAdapter.getFolderViewHolderPosition(currentShowingFolder);
            drawerRecyclerView.scrollToPosition(position);
            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    clearHighLightBackground();
                    NoteDrawerRecyclerViewAdapter.MyViewHolder myViewHolder = (NoteDrawerRecyclerViewAdapter.MyViewHolder) drawerRecyclerView.findViewHolderForAdapterPosition(position);
                    highLightedDrawerButton = myViewHolder.getButton();
                    setHighLightBackground();
                    return false;
                }
            });
            handler.sendEmptyMessageDelayed(1,100);
        } else {
            clearHighLightBackground();
            highLightedDrawerButton = showAllNote;
            setHighLightBackground();
        }
    }

    void init() {

        slideNoteDatabaseHelper = new SlideNoteDatabaseHelper(this,"note",null,1);
        slideNoteDatabaseHelper.clearAllTable();
        slideNoteDatabaseHelper.insertNote(new NoteInfo("null","testFolder","testTime","testName1"));
        slideNoteDatabaseHelper.insertNote(new NoteInfo("null","testFolder","testTime","testName2"));
        slideNoteDatabaseHelper.insertNote(new NoteInfo("null","testFolder","testTime","testName3"));
        slideNoteDatabaseHelper.insertNote(new NoteInfo("null","testFolder3","testTime","testName4"));
        slideNoteDatabaseHelper.insertNote(new NoteInfo("null","testFolder4","testTime","testName5"));
        slideNoteDatabaseHelper.insertNote(new NoteInfo("null","testFolder1","testTime","testName3"));
        slideNoteDatabaseHelper.insertNote(new NoteInfo("null","testFolder2","testTime","testName4"));
        slideNoteDatabaseHelper.insertNote(new NoteInfo("null","testFolder","testTime","testName5"));

        fromPosition = -1;
        toPosition = -1;
        currentShowingFolder = "allNote";
        isSelectMode = false;
        showAllNote.setOnClickListener(this);
        popUpMenuDelete.setOnClickListener(this);
        jumpToSlide.setOnClickListener(this);
        popUpMenuShare.setOnClickListener(this);
        addNewNotebook.setOnClickListener(this);
        popUpMenuMoveTo.setOnClickListener(this);
        highLightedDrawerButton = (Button) findViewById(R.id.showAllNote);
        GlobalStorage.getInstance().setHighLightedNoteDrawerButton(-1);
        bottomShareDialog = new Dialog(this,R.style.MaterialDialogSheet);
    }

    void setDialog(){
        View view = getLayoutInflater().inflate(R.layout.bottom_dialog,null);
        bottomShareDialog.setContentView(view);
        bottomShareDialog.setCancelable(true);
        bottomShareDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomShareDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarMenu.setOnClickListener(this);
        toolbarSelect.setOnClickListener(this);
    }

    void setRecyclerView() {
        drawerRecyclerViewAdapter = new NoteDrawerRecyclerViewAdapter(this);
        drawerRecyclerViewAdapter.setOnNoteFolderClickedListener(this);

        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerRecyclerView.setAdapter(drawerRecyclerViewAdapter);

        contentRecyclerViewAdapter = new NoteContentRecyclerViewAdapter(this);
        contentRecyclerViewAdapter.setOnNoteClickedListener(this);

        allNoteBadge.setText(GlobalStorage.getInstance().getNoteFolderSize("allNote"));

        contentRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        contentRecyclerView.setAdapter(contentRecyclerViewAdapter);

        contentRecyclerViewAdapter.setNoteFolder(currentShowingFolder);

        DragItemTouchHelper dragItemTouchHelper = new DragItemTouchHelper(new DragItemTouchHelperCallback(new DragItemTouchHelperCallback.OnItemTouchCallbackListener() {

            @Override
            public void chooseDropTarget(RecyclerView.ViewHolder selected, RecyclerView.ViewHolder winner) {
                fromPosition = selected.getAdapterPosition();
                if (winner == null){
                    if (contentRecyclerView.findViewHolderForAdapterPosition(toPosition) != null){
                        NoteContentRecyclerViewAdapter.MyViewHolder myViewHolder = (NoteContentRecyclerViewAdapter.MyViewHolder) contentRecyclerView.findViewHolderForAdapterPosition(toPosition);
                        myViewHolder.getNoteThumbnail().setBackgroundColor(Color.parseColor("#9b9b9b"));
                        toPosition = -1;
                    }
                } else {
                    if (winner.getAdapterPosition() == 0){
                        return;
                    }
                    if (toPosition != winner.getAdapterPosition()){
                        NoteContentRecyclerViewAdapter.MyViewHolder myViewHolder = (NoteContentRecyclerViewAdapter.MyViewHolder) contentRecyclerView.findViewHolderForAdapterPosition(toPosition);
                        if (myViewHolder != null){
                            myViewHolder.getNoteThumbnail().setBackgroundColor(Color.parseColor("#9b9b9b"));
                        }
                        toPosition = winner.getAdapterPosition();
                        myViewHolder = (NoteContentRecyclerViewAdapter.MyViewHolder) winner;
                        myViewHolder.getNoteThumbnail().setBackgroundColor(Color.parseColor("#66ccff"));
                    }

                }
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == 0){
                    if (toPosition != -1){
                        Log.e("TestChoosed", String.valueOf(toPosition));
                        NoteContentRecyclerViewAdapter.MyViewHolder myViewHolder = (NoteContentRecyclerViewAdapter.MyViewHolder) contentRecyclerView.findViewHolderForAdapterPosition(toPosition);
                        myViewHolder.getNoteThumbnail().setBackgroundColor(Color.parseColor("#9b9b9b"));
                        NoteContentRecyclerViewAdapter.MyViewHolder srcViewHolder = (NoteContentRecyclerViewAdapter.MyViewHolder) contentRecyclerView.findViewHolderForAdapterPosition(fromPosition);
                        srcViewHolder.getNoteThumbnail().setVisibility(View.GONE);
                        contentRecyclerViewAdapter.remove(fromPosition);
                        slideNoteDatabaseHelper.deleteNote(contentRecyclerViewAdapter.getNoteFolder().get(fromPosition -1).getNoteID());
                        GlobalStorage.getInstance().getNoteFromDataBase(getApplicationContext());
                        allNoteBadge.setText(GlobalStorage.getInstance().getNoteFolderSize("allNote"));
                        drawerRecyclerViewAdapter.updateDateSet();
                        // Todo
                        fromPosition = -1;
                        toPosition = -1;
                    }
                }
            }


        }));
        dragItemTouchHelper.attachToRecyclerView(contentRecyclerView);
    }


    void showDialog(){
        createNewFolderDialog = new Dialog(this);
        createNewFolderDialog.setCancelable(true);
        createNewFolderDialog.setContentView(R.layout.add_one_dialog);
        createNewFolderDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        createNewFolderDialog.getWindow().setDimAmount(0.4f);
        createNewFolderEditText = (EditText) createNewFolderDialog.findViewById(R.id.inputNewFolderName);
        createNewFolderCheck = (TextView) createNewFolderDialog.findViewById(R.id.createNewFolderCheck);
        createNewFolderCancel = (TextView) createNewFolderDialog.findViewById(R.id.createNewFolderCancel);
        createNewFolderCheck.setOnClickListener(this);
        createNewFolderCancel.setOnClickListener(this);
        createNewFolderDialog.show();
    }


    void clearHighLightBackground(){
        if (highLightedDrawerButton == null){
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showAllNote:
                currentShowingFolder = "allNote";
                contentRecyclerViewAdapter.setNoteFolder(currentShowingFolder);
                contentRecyclerViewAdapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                clearHighLightBackground();
                highLightedDrawerButton = (Button) findViewById(R.id.showAllNote);
                GlobalStorage.getInstance().setHighLightedNoteDrawerButton(-1);
                setHighLightBackground();
                break;
            case R.id.popUpMenuDelete:
                GlobalStorage.getInstance().deleteSelectedNote(getApplicationContext());
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
                    TransitionManager.beginDelayedTransition(drawerLayoutContent, autoTransition);
                    popUpMenu.setVisibility(View.GONE);
                    GlobalStorage.getInstance().clearSelectedNote();
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
                bottomShareDialog.show();
                break;
            case R.id.addNewNotebook:
                showDialog();
                break;
            case R.id.createNewFolderCheck:
                String folderName = createNewFolderEditText.getText().toString();
                if (!folderName.isEmpty()){
                    slideNoteDatabaseHelper.createNoteFolder(folderName);
                    GlobalStorage.getInstance().getNoteFromDataBase(this);
                    currentShowingFolder = folderName;
                    contentRecyclerViewAdapter.setNoteFolder(currentShowingFolder);
                    notifyAllAdapterDataSetChange();
                    createNewFolderDialog.dismiss();
                }
                break;
            case R.id.popUpMenuMoveTo:
                if (GlobalStorage.getInstance().getSelectedNoteInfo().size() > 0) {
                    Intent intent1 = new Intent(this, NoteMoveToActivity.class);
                    startActivityForResult(intent1, 100);
                }
                else {
                    Log.e("selectedNoteInfo","zero");
                }
                break;
            case R.id.createNewFolderCancel:
                createNewFolderDialog.dismiss();
                break;
        }
    }

    @Override
    public void onFolderClicked(NoteFolder clickedFolder, Button button) {
        currentShowingFolder = clickedFolder.getFolderName();
        contentRecyclerViewAdapter.setNoteFolder(clickedFolder.getFolderName());
        contentRecyclerViewAdapter.notifyDataSetChanged();
        drawerLayout.closeDrawer(GravityCompat.START);
        clearHighLightBackground();
        highLightedDrawerButton = button;
        setHighLightBackground();
    }

    @Override
    public void onNoteClicked(NoteInfo clickedNote, View checkBox) {
        if (!isSelectMode) {
            Intent intent = new Intent(this, NoteRichEditor.class);
            intent.putExtra("noteContent",clickedNote.getNoteContent());
            intent.putExtra("noteContent",clickedNote.getNoteContent());
            startActivityForResult(intent,1);
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
        switch (requestCode){
            case 100:
                notifyAllAdapterDataSetChange();
                toolbarSelect.callOnClick();
        }
    }

    @Override
    public void onAddOneClicked() {
        Log.e("TestAddOne","AddOneClicked");
    }
}