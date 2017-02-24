package com.example.cieo233.notetest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.InterpolatorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cieo233 on 2/21/2017.
 */

public class MoveToActivity extends AppCompatActivity implements Interfaces.OnImageMoveToFolderClickedListener, View.OnClickListener {
    @BindView(R.id.cancel)
    ImageView cancel;
    @BindView(R.id.numberOfImage)
    TextView numberOfImage;
    @BindView(R.id.whiteBackground1)
    TextView whiteBackground1;
    @BindView(R.id.whiteBackground2)
    TextView whiteBackground2;
    @BindView(R.id.whiteBackground3)
    TextView whiteBackground3;
    @BindView(R.id.imageThumbnail)
    ImageView imageThumbnail;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.imageThumbnailLayout)
    RelativeLayout thumbnailLayout;


    private ImageMoveToRecyclerViewAdapter imageMoveToRecyclerViewAdapter;
    private LinearLayoutManager linearLayoutManager;
    private Dialog createNewFolderDialog;
    private EditText createNewFolderEditText;
    private TextView createNewFolderCheck;
    private SlideNoteDatabaseHelper slideNoteDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_to);
        ButterKnife.bind(this);
        init();
    }



    void init(){
        GlobalStorage.getInstance().getImageFromContentProvider(this);
        slideNoteDatabaseHelper = new SlideNoteDatabaseHelper(this,"note",null,1);
        imageMoveToRecyclerViewAdapter = new ImageMoveToRecyclerViewAdapter(this);
        imageMoveToRecyclerViewAdapter.setOnImageMoveToFolderClickedListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(imageMoveToRecyclerViewAdapter);
    }

    void setImageThumbnail(){
        if (GlobalStorage.getInstance().getSelectedImageInfo().size() == 1){
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
        createNewFolderDialog.getWindow().setLayout(1080, LinearLayout.LayoutParams.WRAP_CONTENT);
        createNewFolderEditText = (EditText) createNewFolderDialog.findViewById(R.id.inputNewFolderName);
        createNewFolderCheck = (TextView) createNewFolderDialog.findViewById(R.id.createNewFolderCheck);
        createNewFolderCheck.setOnClickListener(this);
        createNewFolderDialog.show();
    }

    @Override
    public void onMoveToFolderClicked(ImageFolder targetFolder, int position, float touchX, float touchY) {
        GlobalStorage.getInstance().moveImageToOtherFolder(this,targetFolder);


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
                imageMoveToRecyclerViewAdapter.updateDateSet();
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
        imageMoveToRecyclerViewAdapter.updateDateSet();
        final int position = imageMoveToRecyclerViewAdapter.getFolderViewHolderPosition(folderName);
        recyclerView.scrollToPosition(position);
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                ImageMoveToRecyclerViewAdapter.MyViewHolder myViewHolder = (ImageMoveToRecyclerViewAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
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
                    slideNoteDatabaseHelper.createImageFolder(folderName);
                    GlobalStorage.getInstance().getImageFromContentProvider(this);
                    notifyAdapterDataSetChange(folderName);
                    createNewFolderDialog.dismiss();
                }
                break;
        }
    }
}
