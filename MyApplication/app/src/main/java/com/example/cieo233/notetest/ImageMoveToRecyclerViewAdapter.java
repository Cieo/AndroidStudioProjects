package com.example.cieo233.notetest;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Cieo233 on 2/22/2017.
 */

public class ImageMoveToRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private HashMap<String, ImageFolder> imageFolders;
    private List<String> keys;
    private Interfaces.OnImageMoveToFolderClickedListener onImageMoveToFolderClickedListener;

    public ImageMoveToRecyclerViewAdapter(Context context) {
        this.context = context;
        this.imageFolders = GlobalStorage.getInstance().getImageFolders();
        keys = new ArrayList<>(imageFolders.keySet());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.move_to_item,null,false));
    }

    public void updateDateSet(){
        this.imageFolders = GlobalStorage.getInstance().getImageFolders();
        keys = new ArrayList<>(imageFolders.keySet());
        notifyDataSetChanged();
    }

    public int getFolderViewHolderPosition(String folderName){
        Log.e("TestGetPosition", String.valueOf(keys.indexOf(folderName)+1));
        return keys.indexOf(folderName)+1;
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        MyViewHolder viewHolder = (MyViewHolder) holder;
        int position = holder.getAdapterPosition();
        if (position != 0){
            ImageFolder imageFolder = imageFolders.get(keys.get(position-1));
            if (imageFolder.size() != 0){
                Glide.with(context).load(imageFolder.get(0).getImageURL()).into(viewHolder.getAlbumThumbnail());
            }else {
                viewHolder.getAlbumThumbnail().setImageResource(R.color.subColor4);
            }
        } else {
            viewHolder.getAlbumThumbnail().setImageResource(R.mipmap.btn_add_h);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        MyViewHolder viewHolder = (MyViewHolder) holder;
        if (position == 0){
            viewHolder.getAlbumName().setText("新建相册");
            viewHolder.getAlbumThumbnail().setImageResource(R.mipmap.btn_add_h);
            viewHolder.getAlbumSize().setText("");
            viewHolder.getFolderItem().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onImageMoveToFolderClickedListener != null){
                        onImageMoveToFolderClickedListener.onCreateNewFolderClicked();
                    }
                }
            });
        } else {
            final ImageFolder imageFolder = imageFolders.get(keys.get(position-1));
            viewHolder.getAlbumName().setText(imageFolder.getFolderName());
            viewHolder.getAlbumSize().setText(String.valueOf(imageFolder.size()));
            if (imageFolder.size()!=0){
                Glide.with(context).load(imageFolder.get(0).getImageURL()).into(viewHolder.getAlbumThumbnail());
            }

            viewHolder.getFolderItem().setOnTouchListener(new View.OnTouchListener() {
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
                        if (onImageMoveToFolderClickedListener!=null && Math.abs(x-motionEvent.getRawX()) < 10 && Math.abs(y-motionEvent.getRawY()) < 10){
                            onImageMoveToFolderClickedListener.onMoveToFolderClicked(imageFolder,position,motionEvent.getRawX(),motionEvent.getRawY());
                        }
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imageFolders.size() + 1;
    }

    public Interfaces.OnImageMoveToFolderClickedListener getOnImageMoveToFolderClickedListener() {
        return onImageMoveToFolderClickedListener;
    }

    public void setOnImageMoveToFolderClickedListener(Interfaces.OnImageMoveToFolderClickedListener onImageMoveToFolderClickedListener) {
        this.onImageMoveToFolderClickedListener = onImageMoveToFolderClickedListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView albumThumbnail;
        private TextView albumName, albumSize;
        private LinearLayout folderItem;

        public void mockTouch(){
            int location[] = new int[2];
            folderItem.getLocationOnScreen(location);
            Log.e("TestLocation",String.valueOf(location[0]+" "+String.valueOf(location[1])));
            folderItem.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),MotionEvent.ACTION_DOWN,location[0]+30,location[1]+30,0));
            folderItem.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),MotionEvent.ACTION_UP,location[0]+30,location[1]+30,0));
        }

        public MyViewHolder(View itemView) {
            super(itemView);
            albumThumbnail = (ImageView) itemView.findViewById(R.id.albumThumbnail);
            albumName = (TextView) itemView.findViewById(R.id.albumName);
            albumSize = (TextView) itemView.findViewById(R.id.albumSize);
            folderItem = (LinearLayout) itemView.findViewById(R.id.folderItem);
        }

        public TextView getAlbumSize() {
            return albumSize;
        }

        public void setAlbumSize(TextView albumSize) {
            this.albumSize = albumSize;
        }

        public LinearLayout getFolderItem() {
            return folderItem;
        }

        public void setFolderItem(LinearLayout folderItem) {
            this.folderItem = folderItem;
        }

        public ImageView getAlbumThumbnail() {
            return albumThumbnail;
        }

        public void setAlbumThumbnail(ImageView albumThumbnail) {
            this.albumThumbnail = albumThumbnail;
        }

        public TextView getAlbumName() {
            return albumName;
        }

        public void setAlbumName(TextView albumName) {
            this.albumName = albumName;
        }
    }
}
