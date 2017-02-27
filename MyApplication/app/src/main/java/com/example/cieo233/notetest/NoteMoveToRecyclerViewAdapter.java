package com.example.cieo233.notetest;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cieo233 on 2/23/2017.
 */

public class NoteMoveToRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private HashMap<String, NoteFolder> noteFolders;
    private List<String> keys;
    private Interfaces.OnNoteMoveToFolderClickedListener onNoteMoveToFolderClickedListener;

    public NoteMoveToRecyclerViewAdapter(Context context) {
        this.context = context;
        this.noteFolders = GlobalStorage.getInstance().getNoteFolders();
        keys = new ArrayList<>(noteFolders.keySet());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.add_one_item,null,false));
        }
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,null,false));
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0?0:1;
    }

    public void updateDateSet(){
        this.noteFolders = GlobalStorage.getInstance().getNoteFolders();
        keys = new ArrayList<>(noteFolders.keySet());
        notifyDataSetChanged();
    }

    public int getFolderViewHolderPosition(String folderName){
        Log.e("TestGetPosition", String.valueOf(keys.indexOf(folderName)+1));
        return keys.indexOf(folderName)+1;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        MyViewHolder viewHolder = (MyViewHolder) holder;
        if (position == 0){
            viewHolder.getAddNewNotebook().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNoteMoveToFolderClickedListener != null){
                        onNoteMoveToFolderClickedListener.onCreateNewFolderClicked();
                    }
                }
            });
        } else {
            final NoteFolder noteFolder = noteFolders.get(keys.get(position-1));
            viewHolder.getButton().setText(noteFolder.getFolderName());
            viewHolder.getBadge().setText(String.valueOf(noteFolder.size()));
            viewHolder.getButton().setOnTouchListener(new View.OnTouchListener() {
                private float x = -1, y = -1;

                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        x = motionEvent.getRawX();
                        y = motionEvent.getRawY();
                        Log.e("TestX",String.valueOf(x));
                        Log.e("TestY",String.valueOf(y));
                    }
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                        Log.e("TestX",  String.valueOf(x)+" "+String.valueOf(motionEvent.getRawX()));
                        Log.e("TestY",  String.valueOf(y)+" "+String.valueOf(motionEvent.getRawY()));
                        if (onNoteMoveToFolderClickedListener!=null && Math.abs(x-motionEvent.getRawX()) < 10 && Math.abs(y-motionEvent.getRawY()) < 10){
                            onNoteMoveToFolderClickedListener.onMoveToFolderClicked(noteFolder,position,motionEvent.getRawX(),motionEvent.getRawY());
                        }
                    }
                    return true;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return noteFolders.size() + 1;
    }


    public Interfaces.OnNoteMoveToFolderClickedListener getOnNoteMoveToFolderClickedListener() {
        return onNoteMoveToFolderClickedListener;
    }

    public void setOnNoteMoveToFolderClickedListener(Interfaces.OnNoteMoveToFolderClickedListener onNoteMoveToFolderClickedListener) {
        this.onNoteMoveToFolderClickedListener = onNoteMoveToFolderClickedListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private Button button;
        private TextView badge;
        private LinearLayout addNewNotebook;

        public MyViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView.findViewById(R.id.button);
            badge = (TextView) itemView.findViewById(R.id.badge);
            addNewNotebook = (LinearLayout) itemView.findViewById(R.id.addNewNotebook);
        }

        public void mockTouch(){
            int location[] = new int[2];
            button.getLocationOnScreen(location);
            Log.e("TestLocation",String.valueOf(location[0]+" "+String.valueOf(location[1])));
            button.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),MotionEvent.ACTION_DOWN,location[0]+30,location[1]+30,0));
            button.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),MotionEvent.ACTION_UP,location[0]+30,location[1]+30,0));
        }

        public Button getButton() {
            return button;
        }

        public void setButton(Button button) {
            this.button = button;
        }

        public TextView getBadge() {
            return badge;
        }

        public void setBadge(TextView badge) {
            this.badge = badge;
        }

        public LinearLayout getAddNewNotebook() {
            return addNewNotebook;
        }

        public void setAddNewNotebook(LinearLayout addNewNotebook) {
            this.addNewNotebook = addNewNotebook;
        }
    }
}
