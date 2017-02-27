package com.example.cieo233.notetest;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 2/25/2017.
 */

public class NoteMoveToActivity extends AppCompatActivity implements Interfaces.OnNoteMoveToFolderClickedListener, View.OnClickListener{

    @BindView(R.id.cancel)
    ImageView cancel;
    @BindView(R.id.numberOfNote)
    TextView numberOfNote;
    @BindView(R.id.whiteBackground1)
    TextView whiteBackground1;
    @BindView(R.id.whiteBackground2)
    TextView whiteBackground2;
    @BindView(R.id.whiteBackground3)
    TextView whiteBackground3;
    @BindView(R.id.noteThumbnail)
    ImageView noteThumbnail;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.noteThumbnailLayout)
    RelativeLayout thumbnailLayout;

    private NoteMoveToRecyclerViewAdapter noteMoveToRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Dialog createNewFolderDialog;
    private EditText createNewFolderEditText;
    private TextView createNewFolderCheck;
    private TextView createNewFolderCancel;
    private SlideNoteDatabaseHelper slideNoteDatabaseHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_move_to);
        ButterKnife.bind(this);
        init();
    }



    void init(){
        GlobalStorage.getInstance().getNoteFromDataBase(this);
        slideNoteDatabaseHelper = new SlideNoteDatabaseHelper(this,"note",null,1);
        noteMoveToRecyclerViewAdapter = new NoteMoveToRecyclerViewAdapter(this);
        noteMoveToRecyclerViewAdapter.setOnNoteMoveToFolderClickedListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(noteMoveToRecyclerViewAdapter);
        setImageThumbnail();
    }

    void setImageThumbnail(){
        if (GlobalStorage.getInstance().getSelectedNoteInfo().size() == 1){
            whiteBackground2.setVisibility(View.GONE);
            whiteBackground3.setVisibility(View.GONE);
        }else {
            whiteBackground2.setVisibility(View.VISIBLE);
            whiteBackground3.setVisibility(View.VISIBLE);
        }
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
    public void onMoveToFolderClicked(NoteFolder targetFolder, int position, float touchX, float touchY) {
        GlobalStorage.getInstance().moveNoteToOtherFolder(this,targetFolder);


        float childToX = 60;

        thumbnailLayout.setPivotX(0);
        thumbnailLayout.setPivotY(0);
        ObjectAnimator y =  ObjectAnimator.ofFloat(thumbnailLayout,"y",touchY-30);
        ObjectAnimator x =  ObjectAnimator.ofFloat(thumbnailLayout,"x",childToX+30);
        ObjectAnimator scaleX =  ObjectAnimator.ofFloat(thumbnailLayout,"scaleX",1.0f,0.2f);
        ObjectAnimator scaleY =  ObjectAnimator.ofFloat(thumbnailLayout,"scaleY",1.0f,0.2f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(thumbnailLayout,"alpha",1.0f,0.0f);
        y.setAutoCancel(true);
        x.setAutoCancel(true);
        scaleX.setAutoCancel(true);
        scaleY.setAutoCancel(true);
        alpha.setAutoCancel(true);
        y.setDuration(1000);
        x.setDuration(1000);
        scaleX.setDuration(600);
        scaleY.setDuration(600);
        alpha.setDuration(900);
        y.start();
        x.start();
        scaleX.start();
        scaleY.start();
        alpha.start();
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                noteMoveToRecyclerViewAdapter.updateDateSet();
                return false;
            }
        });
        handler.sendEmptyMessageDelayed(1,1000);
    }

    @Override
    public void onCreateNewFolderClicked() {
        showDialog();
    }


    void notifyAdapterDataSetChange(final String folderName){
        noteMoveToRecyclerViewAdapter.updateDateSet();
        final int position = noteMoveToRecyclerViewAdapter.getFolderViewHolderPosition(folderName);
        recyclerView.scrollToPosition(position);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                NoteMoveToRecyclerViewAdapter.MyViewHolder myViewHolder = (NoteMoveToRecyclerViewAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
                myViewHolder.mockTouch();
                return false;
            }
        });
        handler.sendEmptyMessageDelayed(1,100);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.createNewFolderCheck:
                String folderName = createNewFolderEditText.getText().toString();
                if (!folderName.isEmpty()){
                    slideNoteDatabaseHelper.createNoteFolder(folderName);
                    GlobalStorage.getInstance().getNoteFromDataBase(this);
                    notifyAdapterDataSetChange(folderName);
                    createNewFolderDialog.dismiss();
                }
                break;
            case R.id.createNewFolderCancel:
                createNewFolderDialog.dismiss();
                break;
        }
    }
}
